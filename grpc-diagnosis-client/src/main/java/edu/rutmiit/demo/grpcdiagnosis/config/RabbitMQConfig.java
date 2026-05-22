package edu.rutmiit.demo.grpcdiagnosis.config;

import edu.rutmiit.demo.events.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    public static final String DIAGNOSIS_QUEUE = "q.diagnosis.app-created";
    public static final String DIAGNOSIS_DLQ = "q.diagnosis.app-created.dlq";

    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        factory.setDefaultRequeueRejected(false); // при ошибке → DLQ
        return factory;
    }

    @Bean
    public TopicExchange eventsExchange() {
        return ExchangeBuilder
                .topicExchange(RoutingKeys.EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder
                .directExchange(RoutingKeys.EXCHANGE + ".dlx")
                .durable(true)
                .build();
    }

    @Bean
    public Queue diagnosisQueue() {
        return QueueBuilder
                .durable(DIAGNOSIS_QUEUE)
                .deadLetterExchange(RoutingKeys.EXCHANGE + ".dlx")
                .deadLetterRoutingKey(DIAGNOSIS_DLQ)
                .build();
    }

    @Bean
    public Queue diagnosisDlq() {
        return QueueBuilder.durable(DIAGNOSIS_DLQ).build();
    }

    @Bean
    public Binding diagnosisBinding(Queue diagnosisQueue, TopicExchange eventsExchange) {
        return BindingBuilder
                .bind(diagnosisQueue)
                .to(eventsExchange)
                .with(RoutingKeys.APPOINTMENT_CREATED);
    }

    @Bean
    public Binding diagnosisDlqBinding(Queue diagnosisDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(diagnosisDlq)
                .to(deadLetterExchange)
                .with(DIAGNOSIS_DLQ);
    }
}