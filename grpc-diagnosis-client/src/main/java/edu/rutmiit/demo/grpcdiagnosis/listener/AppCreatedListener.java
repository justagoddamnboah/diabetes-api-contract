package edu.rutmiit.demo.grpcdiagnosis.listener;

import edu.rutmiit.demo.events.AppointmentEvent;
import edu.rutmiit.demo.events.EventMetadata;
import edu.rutmiit.demo.grpc.AnalyzeAppRequest;
import edu.rutmiit.demo.grpc.AppAnalysisResponse;
import edu.rutmiit.demo.grpc.AppAnalyticsGrpc;
import edu.rutmiit.demo.grpcdiagnosis.publisher.DiagnosisEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
public class AppCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(AppCreatedListener.class);

    private final AppAnalyticsGrpc.AppAnalyticsBlockingStub analyticsStub;
    private final DiagnosisEventPublisher diagnosisPublisher;
    private final JsonMapper jsonMapper;

    public AppCreatedListener(AppAnalyticsGrpc.AppAnalyticsBlockingStub analyticsStub,
                              DiagnosisEventPublisher diagnosisPublisher,
                              JsonMapper jsonMapper) {
        this.analyticsStub = analyticsStub;
        this.diagnosisPublisher = diagnosisPublisher;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.diagnosis.app-created", messageConverter = "")
    public void handleAppCreated(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            AppointmentEvent.Created appCreated = jsonMapper.treeToValue(payloadNode, AppointmentEvent.Created.class);

            log.info("Получено событие appointment.created: appId={}, «{}» [eventId={}]",
                    appCreated.appId(), appCreated.appointmentTime(), metadata.eventId());

            AnalyzeAppRequest grpcRequest = AnalyzeAppRequest.newBuilder()
                    .setAppId(appCreated.appId())
                    .setBloodSugar(appCreated.bloodSugar())
                    .setOnDiet(appCreated.onDiet() != null ? appCreated.onDiet() : false)
                    .setFasting(appCreated.fasting() != null ? appCreated.fasting() : true)
                    .build();

            log.info("Вызов gRPC: AppAnalytics.AnalyzeApp(appId={})", appCreated.appId());
            AppAnalysisResponse grpcResponse = analyticsStub.analyzeApp(grpcRequest);

            log.info("gRPC ответ получен: appId={}, диагноз: {}",
                    grpcResponse.getAppId(),
                    grpcResponse.getDiagnosis());

            AppointmentEvent.Diagnosed diagnosedEvent = new AppointmentEvent.Diagnosed(
                    grpcResponse.getAppId(),
                    appCreated.appointmentTime(),
                    grpcResponse.getDiagnosis()
            );

            diagnosisPublisher.publishDiagnosed(diagnosedEvent);

            log.info("Прием проанализирован: appId={}, «{}» и appointment.diagnosed отправлено",
                    appCreated.appId(), appCreated.appointmentTime());

        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка при анализе приема: {} ({})",
                    e.getStatus().getDescription(), e.getStatus().getCode());
            throw new RuntimeException("gRPC-вызов завершился ошибкой", e);

        } catch (Exception e) {
            log.error("Ошибка обработки события appoinment.created: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие appointment.created", e);
        }
    }
}