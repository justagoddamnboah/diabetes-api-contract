package edu.rutmiit.demo.notificationservice.listener;

import edu.rutmiit.demo.events.*;
import edu.rutmiit.demo.notificationservice.websocket.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationListener.class);

    private final NotificationWebSocketHandler webSocketHandler;
    private final JsonMapper jsonMapper;

    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();

    public EventNotificationListener(NotificationWebSocketHandler webSocketHandler,
                                     JsonMapper jsonMapper) {
        this.webSocketHandler = webSocketHandler;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.notifications.all", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            if (!processedEventIds.add(metadata.eventId())) {
                log.warn("Дубликат уведомления пропущен: eventId={}", metadata.eventId());
                return;
            }

            JsonNode payloadNode = root.get("payload");
            String title = buildTitle(metadata.eventType());
            String description = buildDescription(metadata.eventType(), payloadNode);
            String icon = resolveIcon(metadata.eventType());
            String level = resolveLevel(metadata.eventType());

            String notificationJson = jsonMapper.writeValueAsString(
                    new NotificationPayload(
                            "NOTIFICATION",
                            metadata.eventId(),
                            metadata.eventType(),
                            title,
                            description,
                            icon,
                            level,
                            metadata.source(),
                            metadata.timestamp().toString(),
                            Instant.now().toString()
                    )
            );

            webSocketHandler.broadcast(notificationJson);

            log.info("[NOTIFY] {} | {} (клиентов: {})",
                    metadata.eventType(), description, webSocketHandler.getActiveConnectionCount());

        } catch (Exception e) {
            log.error("Ошибка обработки события для уведомлений: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }

    private String buildTitle(String eventType) {
        return switch (eventType) {
            case "appointment.created"   -> "Новый прием";
            case "appointment.updated"   -> "Прием обновлен";
            case "appointment.deleted"   -> "Прием удален";
            case "appointment.diagnosed" -> "Постановка диагноза";
            case "patient.created"       -> "Новый пациент";
            case "patient.deleted"       -> "Пациент удалён";
            default                      -> "Событие: " + eventType;
        };
    }

    private String buildDescription(String eventType, JsonNode payload) {
        try {
            return switch (eventType) {
                case "appointment.created" -> {
                    AppointmentEvent.Created e = jsonMapper.treeToValue(payload, AppointmentEvent.Created.class);
                    yield "Создан прием «%s» (время: %s), пациент: %s".formatted(
                            e.appId(), e.appointmentTime(), e.patientFullName());
                }
                case "appointment.updated" -> {
                    AppointmentEvent.Updated e = jsonMapper.treeToValue(payload, AppointmentEvent.Updated.class);
                    yield "Обновлен прием id=%d (время: %s)".formatted(e.appId(), e.appointmentTime());
                }
                case "appointment.deleted" -> {
                    AppointmentEvent.Deleted e = jsonMapper.treeToValue(payload, AppointmentEvent.Deleted.class);
                    yield "Удален прием id=%d «%s»".formatted(e.appId(), e.appointmentTime());
                }
                case "appointment.diagnosed" -> {
                    AppointmentEvent.Diagnosed e = jsonMapper.treeToValue(payload, AppointmentEvent.Diagnosed.class);
                    yield "Прием (время: %s) — диагноз: %s".formatted(
                            e.appointmentTime(), e.diagnosis());
                }
                case "patient.created" -> {
                    PatientEvent.Created e = jsonMapper.treeToValue(payload, PatientEvent.Created.class);
                    yield "Создан пациент %d (%s), возраст: %d".formatted(
                            e.patientId(), e.fullName(), e.age());
                }
                case "patient.deleted" -> {
                    PatientEvent.Deleted e = jsonMapper.treeToValue(payload, PatientEvent.Deleted.class);
                    yield "Удалён пациент %d (%s) (удалено приемов: %d)".formatted(
                            e.patientId(), e.fullName(), e.deletedAppsCount());
                }
                default -> "Неизвестное событие: " + eventType;
            };
        } catch (Exception e) {
            return "Событие " + eventType + " (ошибка парсинга)";
        }
    }

    private String resolveIcon(String eventType) {
        return switch (eventType) {
            case "appointment.created"   -> "app-plus";
            case "appointment.updated"   -> "app-edit";
            case "appointment.deleted"   -> "app-remove";
            case "appointment.diagnosed"  -> "diagnosis";
            case "patient.created" -> "user-plus";
            case "patient.deleted" -> "user-remove";
            default               -> "bell";
        };
    }

    private String resolveLevel(String eventType) {
        return switch (eventType) {
            case "appointment.deleted", "patient.deleted" -> "warning";
            case "appointment.diagnosed"                  -> "info";
            default                                       -> "success";
        };
    }

    record NotificationPayload(
            String type,
            String eventId,
            String eventType,
            String title,
            String description,
            String icon,
            String level,
            String source,
            String eventTimestamp,
            String receivedAt
    ) {}
}