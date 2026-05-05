package edu.rutmiit.demo.demorest.event;

import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;
import edu.rutmiit.demo.events.EventEnvelope;
import edu.rutmiit.demo.events.PatientEvent;
import edu.rutmiit.demo.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PatientEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PatientEventPublisher.class);
    private static final String SOURCE = "demo-rest";

    private final RabbitTemplate rabbitTemplate;

    public PatientEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(PatientResponse patient) {
        var event = new PatientEvent.Created(
            patient.getId(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getMiddleName(),
            patient.getFullName(),
            patient.getAge()
        );
        send(RoutingKeys.PATIENT_CREATED, event);
    }

    public void publishDeleted(PatientResponse patient, int deletedAppsCount) {
        var event = new PatientEvent.Deleted(
            patient.getId(),
            patient.getFullName(),
            deletedAppsCount
        );
        send(RoutingKeys.PATIENT_DELETED, event);
    }

    private void send(String routingKey, PatientEvent event) {
        try {
            EventEnvelope<PatientEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}