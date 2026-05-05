package edu.rutmiit.demo.demorest.event;

import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.events.AppointmentEvent;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AppointmentEventPublisher.class);
    private static final String SOURCE = "demo-rest";

    private final RabbitTemplate rabbitTemplate;

    public AppointmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(AppointmentResponse app) {
        var event = new AppointmentEvent.Created(
            app.getId(),
            app.getAppointmentTime(),
            app.getBloodSugar(),
            app.getPatient() != null ? app.getPatient().getId() : null,
            app.getPatient() != null ? app.getPatient().getFullName() : "Неизвестен",
            app.isFasting(),
            app.isOnDiet()
        );
        send(RoutingKeys.APPOINTMENT_CREATED, event);
    }

    public void publishUpdated(AppointmentResponse app) {
        var event = new AppointmentEvent.Updated(
            app.getId(),
            app.getAppointmentTime(),
            app.getBloodSugar(),
            app.isFasting(),
            app.isOnDiet()
        );
        send(RoutingKeys.APPOINTMENT_UPDATED, event);
    }

    public void publishDeleted(Long appId, String appointmentTime) {
        var event = new AppointmentEvent.Deleted(appId, appointmentTime);
        send(RoutingKeys.APPOINTMENT_DELETED, event);
    }

    private void send(String routingKey, AppointmentEvent event) {
        try {
            EventEnvelope<AppointmentEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}