package edu.rutmiit.demo.diabetesapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "appointments", itemRelation = "appointment")
@Schema(description = "Информация о приеме")
public class AppSummaryResponse extends RepresentationModel<AppSummaryResponse> {

    @Schema(description = "Уникальный идентификатор приема", example = "1")
    private final Long id;

    @Schema(description = "Запланированное время приема", example = "2025-06-14 10:15:00")
    private final String appointmentTime;

    @Schema(description = "Пациент на этом приеме")
    private final PatientResponse patient;
}