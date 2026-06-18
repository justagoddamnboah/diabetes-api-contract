package edu.rutmiit.demo.demorest.assemblers;

import edu.rutmiit.demo.diabetesapicontract.dto.AppSummaryResponse;
import edu.rutmiit.demo.demorest.controllers.AppointmentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppSummaryModelAssembler implements RepresentationModelAssembler<AppSummaryResponse, EntityModel<AppSummaryResponse>> {

    @Override
    public EntityModel<AppSummaryResponse> toModel(AppSummaryResponse appointment) {
        EntityModel<AppSummaryResponse> model = EntityModel.of(appointment,
            linkTo(methodOn(AppointmentController.class).getAppointmentById(appointment.getId())).withSelfRel(),
            linkTo(methodOn(AppointmentController.class).getAllAppointments(null, 0, 20)).withRel("enhanced collection")
        );
        return model;
    }
}