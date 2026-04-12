package edu.rutmiit.demo.demorest.assemblers;

import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;
import edu.rutmiit.demo.demorest.controllers.PatientController;
import edu.rutmiit.demo.demorest.controllers.AppointmentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PatientModelAssembler implements RepresentationModelAssembler<PatientResponse, EntityModel<PatientResponse>> {

    @Override
    public EntityModel<PatientResponse> toModel(PatientResponse patient) {
        return EntityModel.of(patient,
                linkTo(methodOn(PatientController.class).getPatientById(patient.getId())).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getAllAppointments(patient.getId(), 0, 20)).withRel("appointments"),
                linkTo(methodOn(PatientController.class).getAllPatients(0, 20)).withRel("collection"),
                linkTo(methodOn(PatientController.class).updatePatient(patient.getId(), null)).withRel("update"),
                linkTo(methodOn(PatientController.class).deletePatient(patient.getId())).withRel("delete"),
                linkTo(methodOn(PatientController.class).searchByName(patient.getFullName(), 0, 20)).withRel("search")
        );
    }
}