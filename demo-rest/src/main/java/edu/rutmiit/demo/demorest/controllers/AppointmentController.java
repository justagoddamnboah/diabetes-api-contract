package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.diabetesapicontract.dto.*;
import edu.rutmiit.demo.diabetesapicontract.endpoints.AppointmentApi;
import edu.rutmiit.demo.demorest.assemblers.AppointmentModelAssembler;
import edu.rutmiit.demo.demorest.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController implements AppointmentApi {

    private final AppointmentService appointmentService;
    private final AppointmentModelAssembler appointmentModelAssembler;
    private final PagedResourcesAssembler<AppointmentResponse> pagedResourcesAssembler;

    public AppointmentController(AppointmentService appointmentService, AppointmentModelAssembler appointmentModelAssembler,
                                 PagedResourcesAssembler<AppointmentResponse> pagedResourcesAssembler) {
        this.appointmentService = appointmentService;
        this.appointmentModelAssembler = appointmentModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<AppointmentResponse> getAppointmentById(Long id) {
        return appointmentModelAssembler.toModel(appointmentService.findAppointmentById(id));
    }

    @Override
    public PagedModel<EntityModel<AppointmentResponse>> getAllAppointments(Long patientId, int page, int size) {
        PagedResponse<AppointmentResponse> paged = appointmentService.findAllAppointments(patientId, page, size);
        Page<AppointmentResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedResourcesAssembler.toModel(springPage, appointmentModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<AppointmentResponse>> createAppointment(AppointmentRequest request) {
        AppointmentResponse created = appointmentService.createAppointment(request);
        EntityModel<AppointmentResponse> model = appointmentModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<AppointmentResponse> updateAppointment(Long id, UpdateAppointmentRequest request) {
        return appointmentModelAssembler.toModel(appointmentService.updateAppointment(id, request));
    }

    @Override
    public EntityModel<AppointmentResponse> patchAppointment(Long id, PatchAppointmentRequest request) {
        return appointmentModelAssembler.toModel(appointmentService.patchAppointment(id, request));
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentService.deleteAppointment(id);
    }
}