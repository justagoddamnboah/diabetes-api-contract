package edu.rutmiit.demo.diabetesapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "appointments", itemRelation = "appointment")
@Schema(description = "Информация о приеме")
public class AppointmentResponse extends RepresentationModel<AppointmentResponse> {

    @Schema(description = "Уникальный идентификатор приема", example = "1")
    private final Long id;

    @Schema(description = "Пациент на этом приеме")
    private final PatientResponse patient;

    @Schema(description = "Запланированное время приема", example = "2025-06-14 10:15:00")
    private final String appointmentTime;

    @Schema(description = "Уровень сахара в крови, ммоль/л", example = "5.6")
    private final float bloodSugar;

    @Schema(description = "Пациент на диете?")
    private final boolean onDiet;

    @Schema(description = "Пациент пришел натощак?")
    private final boolean fasting;

    @Schema(description = "Момент создания приема")
    private final LocalDateTime createdAt;

    @Schema(description = "Момент последнего обновления приема")
    private final LocalDateTime updatedAt;
}