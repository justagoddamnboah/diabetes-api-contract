package edu.rutmiit.demo.demorest.graphql.types;

public record CreateAppointmentInputGql(
        String patientId,
        Float bloodSugar,
        String appointmentTime,
        Boolean onDiet,
        Boolean fasting
) {}
