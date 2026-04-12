package edu.rutmiit.demo.diabetesapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Частичное обновление пациента (PATCH). Передайте только те поля, которые нужно изменить.")
public record PatchPatientRequest(

        @Schema(description = "Новое имя пациента", example = "Алексей")
        @Size(max = 100, message = "Имя не может превышать 100 символов")
        String firstName,

        @Schema(description = "Новая фамилия пациента", example = "Петров")
        @Size(max = 100, message = "Фамилия не может превышать 100 символов")
        String lastName,

        @Schema(description = "Новое отчество пациента", example = "Александрович")
        @Size(max = 100, message = "Отчество не может превышать 100 символов")
        String middleName,

        @Schema(description = "Новый возраст пациента", example = "46")
        @Positive(message = "Возраст не может быть отрицательным")
        Integer age
) {}