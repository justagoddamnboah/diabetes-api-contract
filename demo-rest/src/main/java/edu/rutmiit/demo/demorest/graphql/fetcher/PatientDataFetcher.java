package edu.rutmiit.demo.demorest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.demorest.graphql.types.*;
import edu.rutmiit.demo.diabetesapicontract.dto.PagedResponse;
import edu.rutmiit.demo.demorest.service.PatientService;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientRequest;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;

@DgsComponent
public class PatientDataFetcher {

    private final PatientService patientService;

    public PatientDataFetcher(PatientService patientService) {
        this.patientService = patientService;
    }

    @DgsQuery
    public PatientResponse patient(@InputArgument String id) {
        return patientService.findById(Long.parseLong(id));
    }

    @DgsQuery
    public PatientConnectionGql patients(
        @InputArgument PatientFilterGql filter,
        @InputArgument Integer page,
        @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        String nameSearch = null;

        if (filter != null) {
            nameSearch = filter.nameSearch();
        }

        PagedResponse<PatientResponse> paged = patientService.findAll(nameSearch, pageNum, pageSize);

        return new PatientConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }

    @DgsMutation
    public PatientResponse createPatient(@InputArgument CreatePatientInputGql input) {
        PatientRequest request = new PatientRequest(
                input.firstName(),
                input.lastName(),
                input.middleName(),
                input.age()
        );
        return patientService.create(request);
    }

    @DgsMutation
    public PatientResponse updatePatient(@InputArgument String id, @InputArgument UpdatePatientInputGql input) {
        PatientRequest request = new PatientRequest(
            input.firstName(),
            input.lastName(),
            input.middleName(),
            input.age()
        );
        return patientService.update(Long.parseLong(id), request);
    }

    @DgsMutation
    public boolean deletePatient(@InputArgument String id) {
        patientService.delete(Long.parseLong(id));
        return true;
    }
}