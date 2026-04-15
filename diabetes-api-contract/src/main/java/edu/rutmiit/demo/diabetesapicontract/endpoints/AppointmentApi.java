package edu.rutmiit.demo.diabetesapicontract.endpoints;

import edu.rutmiit.demo.diabetesapicontract.config.DiabetesApiContractConfig;
import edu.rutmiit.demo.diabetesapicontract.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Appointments", description = "Управление приемами")
@RequestMapping(
        value = "/api/appointments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface AppointmentApi {

    @Operation(
            summary = "Получить прием по ID",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Прием найден")
    @ApiResponse(responseCode = "404", description = "Прием не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<AppointmentResponse> getAppointmentById(
            @Parameter(description = "ID приема", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Список приемов",
            description = """
                    Возвращает постраничный список приемов с HATEOAS-ссылками.
                    Поддерживает комбинирование фильтров: patientId, appointmentTime, bloodSugar
                    можно передавать одновременно.
                    """,
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список приемов")
    @GetMapping
    PagedModel<EntityModel<AppointmentResponse>> getAllAppointments(
            @Parameter(description = "Фильтр по ID пациента") @RequestParam(required = false) Long patientId,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Создать прием",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Прием создан. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пациент с указанным patientId не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Прием с таким временем уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<AppointmentResponse>> createAppointment(@Valid @RequestBody AppointmentRequest request);

    @Operation(
            summary = "Полное обновление приема (PUT)",
            description = "Заменяет все поля приема. Пациента изменить нельзя. "
                    + "Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Прием обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Прием не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Прием с таким временем уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<AppointmentResponse> updateAppointment(
            @Parameter(description = "ID приема", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request
    );

    @Operation(
            summary = "Частичное обновление приема (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений. Пациента приема изменить нельзя.
                    """,
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Прием обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Прием не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Прием с таким временем уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<AppointmentResponse> patchAppointment(
            @Parameter(description = "ID приема", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchAppointmentRequest request
    );

    @Operation(
            summary = "Удалить прием",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Прием удален")
    @ApiResponse(responseCode = "404", description = "Прием не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAppointment(
            @Parameter(description = "ID приема", required = true, example = "1") @PathVariable Long id
    );

    @Operation(summary = "Краткий список всех приемов (без деталей)")
    @GetMapping("/summary")
    List<EntityModel<AppSummaryResponse>> getAllAppsSummary();
}