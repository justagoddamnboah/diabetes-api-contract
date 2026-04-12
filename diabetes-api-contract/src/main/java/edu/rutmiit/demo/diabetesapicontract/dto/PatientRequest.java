package edu.rutmiit.demo.diabetesapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание или полное обновление пациента")
public record PatientRequest(

        @Schema(description = "Имя пациента", example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пациента не может быть пустым")
        @Size(max = 100, message = "Имя не может превышать 100 символов")
        String firstName,

        @Schema(description = "Фамилия пациета", example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия пациента не может быть пустой")
        @Size(max = 100, message = "Фамилия не может превышать 100 символов")
        String lastName,

        @Schema(description = "Отчество пациента", example = "Иванович", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(max = 100, message = "Отчество пациента не может быть пустой")
        String middleName,

        @Schema(description = "Возраст пациента", example = "45")
        @NotNull(message = "У пациента должен быть возраст")
        @Positive(message = "Возраст не можжет быть отрицательным")
        int age
) {}