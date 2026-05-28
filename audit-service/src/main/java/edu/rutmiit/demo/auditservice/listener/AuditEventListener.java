package edu.rutmiit.demo.auditservice.listener;

import edu.rutmiit.demo.auditservice.model.AuditEntry;
import edu.rutmiit.demo.auditservice.storage.AuditStorage;
import edu.rutmiit.demo.events.PatientEvent;
import edu.rutmiit.demo.events.AppointmentEvent;
import edu.rutmiit.demo.events.EventMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

@Component
public class AuditEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);

    private final AuditStorage auditStorage;
    private final JsonMapper jsonMapper;

    public AuditEventListener(AuditStorage auditStorage, JsonMapper jsonMapper) {
        this.auditStorage = auditStorage;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.audit.events", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            if (auditStorage.isDuplicate(metadata.eventId())) {
                log.warn("Дубликат события пропущен: eventId={}", metadata.eventId());
                return;
            }

            JsonNode payloadNode = root.get("payload");
            String description = buildDescription(metadata.eventType(), payloadNode);

            AuditEntry entry = auditStorage.save(new AuditEntry(
                    0,
                    metadata.eventId(),
                    metadata.eventType(),
                    metadata.source(),
                    metadata.timestamp(),
                    Instant.now(),
                    description
            ));

            log.info("[AUDIT #{}] {} | {}", entry.sequenceNumber(), metadata.eventType(), description);

        } catch (Exception e) {
            log.error("Ошибка обработки события: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }

    private String buildDescription(String eventType, JsonNode payloadNode) throws Exception {
        return switch (eventType) {
            case "appointment.created" -> {
                AppointmentEvent.Created e = jsonMapper.treeToValue(payloadNode, AppointmentEvent.Created.class);
                yield String.format("Создан прием: «%s», пациент: %s",
                        e.appointmentTime(), e.patientFullName());
            }
            case "appointment.updated" -> {
                AppointmentEvent.Updated e = jsonMapper.treeToValue(payloadNode, AppointmentEvent.Updated.class);
                yield String.format("Обновлен прием id=%d «%s»", e.appId(), e.appointmentTime());
            }
            case "appointment.deleted" -> {
                AppointmentEvent.Deleted e = jsonMapper.treeToValue(payloadNode, AppointmentEvent.Deleted.class);
                yield String.format("Удален прием id=%d «%s»", e.appId(), e.appointmentTime());
            }
            case "patient.created" -> {
                PatientEvent.Created e = jsonMapper.treeToValue(payloadNode, PatientEvent.Created.class);
                yield String.format("Создан пациент «%s» (возраст: %d)",
                        e.fullName(), e.age());
            }
            case "patient.deleted" -> {
                PatientEvent.Deleted e = jsonMapper.treeToValue(payloadNode, PatientEvent.Deleted.class);
                yield String.format("Удалён пациент «%s» (удалено приемов: %d)",
                        e.fullName(), e.deletedAppsCount());
            }
            case "appointment.diagnosed" -> {
                AppointmentEvent.Diagnosed e = jsonMapper.treeToValue(payloadNode, AppointmentEvent.Diagnosed.class);
                yield String.format("Прием проанализирован id=%d [%s] (Диагноз: %s)",
                    e.appId(), e.appointmentTime(), e.diagnosis());
            }
            default -> "Неизвестное событие: " + eventType;
        };
    }
}