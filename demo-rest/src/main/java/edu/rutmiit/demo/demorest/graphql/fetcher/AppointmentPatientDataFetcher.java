package edu.rutmiit.demo.demorest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import edu.rutmiit.demo.demorest.service.PatientService;
import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;

@DgsComponent
public class AppointmentPatientDataFetcher {

    private final PatientService patientService;

    public AppointmentPatientDataFetcher(PatientService patientService) {
        this.patientService = patientService;
    }

    @DgsData(parentType = "Appointment", field = "patient")
    public PatientResponse patient(DgsDataFetchingEnvironment dfe) {
        AppointmentResponse appointment = dfe.getSource();

        if (appointment.getPatient() != null) {
            return appointment.getPatient();
        }
        return null;
    }
}