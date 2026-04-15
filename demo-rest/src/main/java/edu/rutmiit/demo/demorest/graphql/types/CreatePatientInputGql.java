package edu.rutmiit.demo.demorest.graphql.types;

public record CreatePatientInputGql(
        String firstName,
        String lastName,
        String middleName,
        Integer age
) {}