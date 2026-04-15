package edu.rutmiit.demo.demorest.graphql.types;


import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;

import java.util.List;

public record AppointmentConnectionGql(
        List<AppointmentResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}