package edu.rutmiit.demo.demorest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.PagedResponse;
import edu.rutmiit.demo.demorest.graphql.types.AppointmentConnectionGql;
import edu.rutmiit.demo.demorest.graphql.types.PageInfoGql;
import edu.rutmiit.demo.demorest.service.AppointmentService;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;

@DgsComponent
public class PatientAppointmentsDataFetcher {

    private final AppointmentService appointmentService;

    public PatientAppointmentsDataFetcher(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @DgsData(parentType = "Patient", field = "appointments")
    public AppointmentConnectionGql appointments(
            DgsDataFetchingEnvironment dfe,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        PatientResponse patient = dfe.getSource();

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PagedResponse<AppointmentResponse> paged = appointmentService.findAllAppointments(
                patient.getId(), pageNum, pageSize);

        return new AppointmentConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }
}