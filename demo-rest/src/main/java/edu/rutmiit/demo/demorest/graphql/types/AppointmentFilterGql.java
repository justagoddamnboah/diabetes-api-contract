package edu.rutmiit.demo.demorest.graphql.types;

public record AppointmentFilterGql(
        String patientId,
        Float bloodSugar,
        Boolean onDiet,
        Boolean fasting
) {}