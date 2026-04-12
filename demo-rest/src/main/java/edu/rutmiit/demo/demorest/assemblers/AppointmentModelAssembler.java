package edu.rutmiit.demo.demorest.assemblers;

import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.demorest.controllers.PatientController;
import edu.rutmiit.demo.demorest.controllers.AppointmentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppointmentModelAssembler implements RepresentationModelAssembler<AppointmentResponse, EntityModel<AppointmentResponse>> {

    @Override
    public EntityModel<AppointmentResponse> toModel(AppointmentResponse appointment) {
        EntityModel<AppointmentResponse> model = EntityModel.of(appointment,
                linkTo(methodOn(AppointmentController.class).getAppointmentById(appointment.getId())).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getAllAppointments(null, 0, 20)).withRel("collection")
        );
        if (appointment.getPatient() != null) {
            model.add(linkTo(methodOn(PatientController.class)
                    .getPatientById(appointment.getPatient().getId())).withRel("patient"));
        }
        return model;
    }
}