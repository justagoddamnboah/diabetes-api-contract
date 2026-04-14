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

@Tag(name = "Patients", description = "Управление пациентами")
@RequestMapping(
        value = "/api/patients",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface PatientApi {

    @Operation(
            summary = "Список пациентов",
            description = "Возвращает постраничный список пациентов с HATEOAS-ссылками. "
                    + "Ссылки prev/next позволяют клиенту навигировать по страницам без знания офсетов.",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список пациентов")
    @GetMapping
    PagedModel<EntityModel<PatientResponse>> getAllPatients(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Получить пациента по ID",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Пациент найден")
    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<PatientResponse> getPatientById(
            @Parameter(description = "ID пациента", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Создать пациента",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Пациент создан. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<PatientResponse>> createPatient(@Valid @RequestBody PatientRequest request);

    @Operation(
            summary = "Полное обновление пациента (PUT)",
            description = "Заменяет все поля пациента. Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Пациент обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<PatientResponse> updatePatient(
            @Parameter(description = "ID пациента", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatientRequest request
    );

    @Operation(
            summary = "Частичное обновление пациента (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений.
                    """,
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Пациент обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<PatientResponse> patchPatient(
            @Parameter(description = "ID пациента", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchPatientRequest request
    );

    @Operation(
            summary = "Удалить пациента",
            description = "Удаляет пациента и все его приемы (каскадное удаление).",
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Пациент удалён")
    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    EntityModel<PatientResponse> deletePatient(
            @Parameter(description = "ID Пациента", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
        summary = "Поиск пациентов по имени",
        description = "Возвращает постраничный список пациентов, отфильтрованных по имени.",
        security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список пациентов")
    @ApiResponse(responseCode = "404", description = "Пациенты не найден",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/search")
    PagedModel<EntityModel<PatientResponse>> searchByName(
        @Parameter(description = "Ввод", required = true, example = "Иван") @RequestParam String query,
        @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
        summary = "Вывести пациента и количество его приемов",
        description = "Возвращает пациента и количество его приемов.",
        security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Пациент найден")
    @ApiResponse(responseCode = "404", description = "Пациенты не найден",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/appCount")
    EntityModel<PatientResponse> recalculateAppsCount(
        @Parameter(description = "ID Пациента", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Приемы пациента (суб-ресурс)",
            description = """
                    Возвращает постраничный список приемов указанного пациента.
                    Это суб-ресурс (концепция REST): /patients/{id}/appointments.
                    Эквивалентен GET /appointments?patientId={id}, но точнее отражает иерархию.
                    """,
            security = @SecurityRequirement(name = DiabetesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список приемов пациента")
    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/appointments")
    PagedModel<EntityModel<AppointmentResponse>> getAppointmentsByPatient(
            @Parameter(description = "ID пациента", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );
}