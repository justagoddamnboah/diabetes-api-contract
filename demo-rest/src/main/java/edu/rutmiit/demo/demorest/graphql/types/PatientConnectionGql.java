package edu.rutmiit.demo.demorest.graphql.types;

import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;

import java.util.List;

public record PatientConnectionGql(
        List<PatientResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}