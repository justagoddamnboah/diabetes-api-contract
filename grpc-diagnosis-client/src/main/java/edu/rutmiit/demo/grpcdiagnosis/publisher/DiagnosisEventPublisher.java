package edu.rutmiit.demo.grpcdiagnosis.publisher;

import edu.rutmiit.demo.events.AppointmentEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DiagnosisEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DiagnosisEventPublisher.class);
    private static final String SOURCE = "grpc-diagnosis-client";

    private final RabbitTemplate rabbitTemplate;

    public DiagnosisEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDiagnosed(AppointmentEvent.Analyzed analyzedEvent) {
        try {
            EventEnvelope<AppointmentEvent> envelope = EventEnvelope.wrap(
                analyzedEvent, SOURCE, RoutingKeys.APPOINTMENT_DIAGNOSED);

            rabbitTemplate.convertAndSend(
                RoutingKeys.EXCHANGE,
                RoutingKeys.APPOINTMENT_DIAGNOSED,
                envelope);

            log.info("Событие отправлено: {} [appId={}, eventId={}]",
                RoutingKeys.APPOINTMENT_DIAGNOSED,
                analyzedEvent.appId(),
                envelope.metadata().eventId());

        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}",
                RoutingKeys.APPOINTMENT_DIAGNOSED, e.getMessage());
        }
    }
}