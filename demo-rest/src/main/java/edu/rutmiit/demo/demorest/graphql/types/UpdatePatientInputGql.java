package edu.rutmiit.demo.demorest.graphql.types;

public record UpdatePatientInputGql(
        String firstName,
        String lastName,
        String middleName,
        Integer age
) {}