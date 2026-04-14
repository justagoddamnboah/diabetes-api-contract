package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.diabetesapicontract.dto.PagedResponse;
import edu.rutmiit.demo.demorest.assemblers.PatientModelAssembler;
import edu.rutmiit.demo.demorest.assemblers.AppointmentModelAssembler;
import edu.rutmiit.demo.demorest.service.PatientService;
import edu.rutmiit.demo.demorest.service.AppointmentService;
import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.PatchPatientRequest;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientRequest;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;
import edu.rutmiit.demo.diabetesapicontract.endpoints.PatientApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController implements PatientApi {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final PatientModelAssembler patientModelAssembler;
    private final AppointmentModelAssembler appointmentModelAssembler;
    private final PagedResourcesAssembler<PatientResponse> pagedPatientsAssembler;
    private final PagedResourcesAssembler<AppointmentResponse> pagedAppointmentsAssembler;

    public PatientController(PatientService patientService,
                             AppointmentService appointmentService,
                             PatientModelAssembler patientModelAssembler,
                             AppointmentModelAssembler appointmentModelAssembler,
                             PagedResourcesAssembler<PatientResponse> pagedPatientsAssembler,
                             PagedResourcesAssembler<AppointmentResponse> pagedAppointmentsAssembler) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.patientModelAssembler = patientModelAssembler;
        this.appointmentModelAssembler = appointmentModelAssembler;
        this.pagedPatientsAssembler = pagedPatientsAssembler;
        this.pagedAppointmentsAssembler = pagedAppointmentsAssembler;
    }

    @Override
    public PagedModel<EntityModel<PatientResponse>> getAllPatients(int page, int size) {
        PagedResponse<PatientResponse> paged = patientService.findAll(page, size);
        Page<PatientResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedPatientsAssembler.toModel(springPage, patientModelAssembler);
    }

    public EntityModel<PatientResponse> getPatientById(Long id) {
        return patientModelAssembler.toModel(patientService.findById(id));
    }

    @Override
    public ResponseEntity<EntityModel<PatientResponse>> createPatient(PatientRequest request) {
        PatientResponse created = patientService.create(request);
        EntityModel<PatientResponse> model = patientModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<PatientResponse> updatePatient(Long id, PatientRequest request) {
        return patientModelAssembler.toModel(patientService.update(id, request));
    }

    @Override
    public EntityModel<PatientResponse> patchPatient(Long id, PatchPatientRequest request) {
        return patientModelAssembler.toModel(patientService.patchPatient(id, request));
    }

    @Override
    public EntityModel<PatientResponse> deletePatient(Long id) {
        return patientModelAssembler.toModel(patientService.delete(id));
    }

    @Override
    public EntityModel<PatientResponse> recalculateAppsCount(Long id) {
        return patientModelAssembler.toModel(patientService.recalculateAppsCount(id));
    }

    @Override
    public PagedModel<EntityModel<PatientResponse>> searchByName(String query, int page, int size) {
        PagedResponse<PatientResponse> paged = patientService.searchByName(query, page, size);
        Page<PatientResponse> singlePage = new PageImpl<>(
            paged.content(),
            PageRequest.of(paged.pageNumber(), paged.pageSize()),
            paged.totalElements()
        );
        return pagedPatientsAssembler.toModel(singlePage, patientModelAssembler);
    }

    @Override
    public PagedModel<EntityModel<AppointmentResponse>> getAppointmentsByPatient(Long id, int page, int size) {
        patientService.findById(id);
        PagedResponse<AppointmentResponse> paged = appointmentService.findAllAppointments(id, page, size);
        Page<AppointmentResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedAppointmentsAssembler.toModel(springPage, appointmentModelAssembler);
    }
}