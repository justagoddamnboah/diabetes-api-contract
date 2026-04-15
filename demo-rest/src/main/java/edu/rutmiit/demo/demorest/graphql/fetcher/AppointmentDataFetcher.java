package edu.rutmiit.demo.demorest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.demorest.graphql.types.*;
import edu.rutmiit.demo.demorest.service.AppointmentService;
import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentRequest;
import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.PagedResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.UpdateAppointmentRequest;

@DgsComponent
public class AppointmentDataFetcher {

    private final AppointmentService appointmentService;

    public AppointmentDataFetcher(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @DgsQuery
    public AppointmentResponse appointment(@InputArgument String id) {
        return appointmentService.findAppointmentById(Long.parseLong(id));
    }

    @DgsQuery
    public AppointmentConnectionGql appointments(
            @InputArgument AppointmentFilterGql filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        Long patientId = null;

        if (filter != null) {
            patientId = filter.patientId() != null ? Long.parseLong(filter.patientId()) : null;
        }

        PagedResponse<AppointmentResponse> paged = appointmentService
            .findAllAppointments(patientId, pageNum, pageSize);

        return new AppointmentConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }

    @DgsMutation
    public AppointmentResponse createAppointment(@InputArgument CreateAppointmentInputGql input) {
        AppointmentRequest request = new AppointmentRequest(
                Long.parseLong(input.patientId()),
                input.appointmentTime(),
                input.bloodSugar(),
                input.onDiet(),
                input.fasting()
        );
        return appointmentService.createAppointment(request);
    }

    @DgsMutation
    public AppointmentResponse updateAppointment(@InputArgument String id, @InputArgument UpdateAppointmentInputGql input) {
        UpdateAppointmentRequest request = new UpdateAppointmentRequest(
                input.appointmentTime(),
                input.bloodSugar(),
                input.onDiet(),
                input.fasting()
        );
        return appointmentService.updateAppointment(Long.parseLong(id), request);
    }

    @DgsMutation
    public boolean deleteAppointment(@InputArgument String id) {
        appointmentService.deleteAppointment(Long.parseLong(id));
        return true;
    }
}