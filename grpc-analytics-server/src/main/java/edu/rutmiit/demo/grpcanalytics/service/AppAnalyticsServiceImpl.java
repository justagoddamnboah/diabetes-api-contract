package edu.rutmiit.demo.grpcanalytics.service;

import edu.rutmiit.demo.grpc.AnalyzeAppRequest;
import edu.rutmiit.demo.grpc.AppAnalysisResponse;
import edu.rutmiit.demo.grpc.AppAnalyticsGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppAnalyticsServiceImpl extends AppAnalyticsGrpc.AppAnalyticsImplBase {

    private static final Logger log = LoggerFactory.getLogger(AppAnalyticsServiceImpl.class);

    @Override
    public void analyzeApp(AnalyzeAppRequest request,
                           StreamObserver<AppAnalysisResponse> responseObserver) {

        log.info("gRPC запрос: проверка анализов id={} уровень сахара={} (на диете: {}, натощак: {})",
                request.getAppId(),
                request.getBloodSugar(),
                request.getOnDiet(),
                request.getFasting());

        String diagnosis = whichDiagnosis(request.getBloodSugar(), request.getOnDiet(), request.getFasting());

        AppAnalysisResponse response = AppAnalysisResponse.newBuilder()
                .setAppId(request.getAppId())
                .setDiagnosis(diagnosis)
                .build();

        log.info("gRPC ответ: прием id={}, диагноз: {}", response.getAppId(), diagnosis);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String whichDiagnosis(float bloodSugar, boolean onDiet, boolean fasting) {
        if (onDiet) {
            if (fasting) {
                if (bloodSugar < 3.8) {
                    return "Гипогликемия (диета слишком строгая!)";
                } else if (bloodSugar >= 3.8 && bloodSugar < 5.5) {
                    return "Норма (диета эффективна)";
                } else if (bloodSugar >= 5.5 && bloodSugar < 6.1) {
                    return "Компенсация недостаточная (скорректируйте диету)";
                } else if (bloodSugar >= 6.1 && bloodSugar < 7.0) {
                    return "Преддиабет (диета неэффективна)";
                } else {
                    return "Диабет (требуется медикаментозное лечение)";
                }
            } else {
                if (bloodSugar < 3.9) {
                    return "Гипогликемия (диета слишком строгая!)";
                } else if (bloodSugar >= 3.9 && bloodSugar < 6.5) {
                    return "Норма (диета эффективна)";
                } else if (bloodSugar >= 6.5 && bloodSugar < 7.8) {
                    return "Компенсация недостаточная (скорректируйте диету)";
                } else if (bloodSugar >= 7.8 && bloodSugar < 11.1) {
                    return "Преддиабет (диета неэффективна)";
                } else {
                    return "Диабет (требуется медикаментозное лечение)";
                }
            }
        } else {
            if (fasting) {
                if (bloodSugar < 3.5) {
                    return "Гипогликемия (срочно принять углеводы)";
                } else if (bloodSugar >= 3.5 && bloodSugar < 6.1) {
                    return "Норма (показатели в порядке)";
                } else if (bloodSugar >= 6.1 && bloodSugar < 7.0) {
                    return "Преддиабет (рекомендуется диета)";
                } else {
                    return "Диабет (срочно к врачу!)";
                }
            } else {
                if (bloodSugar < 3.9) {
                    return "Гипогликемия (срочно принять углеводы)";
                } else if (bloodSugar >= 3.9 && bloodSugar < 7.8) {
                    return "Норма (показатели в порядке)";
                } else if (bloodSugar >= 7.8 && bloodSugar < 11.1) {
                    return "Преддиабет (рекомендуется диета)";
                } else {
                    return "Диабет (срочно к врачу!)";
                }
            }
        }
    }
}