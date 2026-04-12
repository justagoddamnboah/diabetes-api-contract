package edu.rutmiit.demo.diabetesapicontract.dto;

import edu.rutmiit.demo.diabetesapicontract.validation.ValidAppointmentTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Полное обновление приема (PUT). Все обязательные поля должны присутствовать. "
        + "Автор книги не меняется.")
public record UpdateAppointmentRequest(

    @ValidAppointmentTime
    @Schema(description = "Новое время приема", example = "2025-06-14 11:15:00")
    String appointmentTime,

    @Schema(description = "Новый уровень сахара", example = "5.1")
    Float bloodSugar,

    @Schema(description = "Так был он на диете или нет?")
    boolean onDiet,

    @Schema(description = "Так натощак он был или нет?")
    boolean fasting
) {}