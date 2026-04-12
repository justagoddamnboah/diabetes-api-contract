package edu.rutmiit.demo.diabetesapicontract.dto;

import edu.rutmiit.demo.diabetesapicontract.validation.ValidAppointmentTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Частичное обновление приема (PATCH). Передайте только те поля, которые нужно изменить. "
        + "Непереданные поля остаются без изменений.")
public record PatchAppointmentRequest(

        @Schema(description = "Новое время приема", example = "2025-06-14 11:15:00")
        @ValidAppointmentTime
        String appointmentTime,

        @Schema(description = "Новый уровень сахара", example = "5.1")
        Float bloodSugar,

        @Schema(description = "Так был он на диете или нет?")
        Boolean onDiet,

        @Schema(description = "Так натощак он был или нет?")
        Boolean fasting
) {}