package edu.rutmiit.demo.diabetesapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "patients", itemRelation = "patient")
@Schema(description = "Информация о пациенте")
@JsonPropertyOrder({"id", "lastName", "firstName", "middleName", "fullName", "age", "appCount"})
public class PatientResponse extends RepresentationModel<PatientResponse> {

    @Schema(description = "Уникальный идентификатор пациента", example = "1")
    private final Long id;

    @Schema(description = "Имя пациента", example = "Иван")
    private final String firstName;

    @Schema(description = "Фамилия пациента", example = "Иванов")
    private final String lastName;

    @Schema(description = "Отчество пациента", example = "Иванович")
    private final String middleName;

    @Schema(description = "Полное имя (lastName + firstName + middleName)", example = "Иванов Иван Иванович")
    private final String fullName;

    @Schema(description = "Возраст пациента", example = "45")
    private final Integer age;

    @Schema(description = "Количество приемов", example = "4")
    private final Integer appCount;
}