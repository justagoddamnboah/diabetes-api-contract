package edu.rutmiit.demo.demorest.graphql.types;

public record UpdateAppointmentInputGql(
        Float bloodSugar,
        String appointmentTime,
        Boolean onDiet,
        Boolean fasting
) {}