package edu.rutmiit.demo.diabetesapicontract.dto;

import edu.rutmiit.demo.diabetesapicontract.validation.ValidAppointmentTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание приема")
public record AppointmentRequest(

        @Schema(description = "ID пациента", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID пациента не может быть пустым")
        Long patientId,

        @Schema(description = "Запланированное время приема", example = "2025-06-14 10:15:00")
        @NotNull(message = "Прием должен иметь время")
        @ValidAppointmentTime
        String appointmentTime,

        @Schema(description = "Уровень сахара в крови, ммоль/л", example = "5.6")
        @NotNull(message = "Уровень сахара должен быть измерен")
        @Min(value = 3, message = "Пациент с таким уровнем сахара должен лежать в реанимации")
        @Max(value = 12, message = "Пациент с таким уровнем сахара должен лежать в реанимации")
        float bloodSugar,

        @Schema(description = "Пациент на диете?")
        @NotNull(message = "Пациент либо на диете, либо нет")
        boolean onDiet,

        @Schema(description = "Пациент пришел натощак?")
        @NotNull(message = "Пациент пришел либо натощак, либо нет")
        boolean fasting
) {}