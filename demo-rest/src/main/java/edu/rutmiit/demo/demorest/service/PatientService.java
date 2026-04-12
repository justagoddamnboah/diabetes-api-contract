package edu.rutmiit.demo.demorest.service;


import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import edu.rutmiit.demo.diabetesapicontract.dto.PagedResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.PatchPatientRequest;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientRequest;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;
import edu.rutmiit.demo.diabetesapicontract.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final InMemoryStorage storage;
    private final AppointmentService appointmentService;

    public PatientService(InMemoryStorage storage, @Lazy AppointmentService appointmentService) {
        this.storage = storage;
        this.appointmentService = appointmentService;
    }

    public PagedResponse<PatientResponse> findAll(int page, int size) {
        List<PatientResponse> all = storage.patients.values().stream()
                .sorted(Comparator.comparingLong(PatientResponse::getId))
                .toList();
        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<PatientResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public PatientResponse findById(Long id) {
        return Optional.ofNullable(storage.patients.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Patient", id));
    }

    public PagedResponse<PatientResponse> searchByName(String query, int page, int size) {
        List<PatientResponse> filtered = storage.patients.values()
            .stream()
            .sorted(Comparator.comparingLong(PatientResponse::getId))
            .filter(patient -> patient.getFullName().toLowerCase().contains(query.toLowerCase()))
            .toList();
        int totalElements = filtered.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<PatientResponse> content = (from >= totalElements) ? List.of() : filtered.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public PatientResponse create(PatientRequest request) {
        long id = storage.patientSequence.incrementAndGet();
        String fullName = request.firstName() + " " + request.lastName();
        if (request.middleName() != null) {
            fullName += " " + request.middleName();
        }
        PatientResponse patient = PatientResponse.builder()
                .id(id)
                .lastName(request.lastName())
                .firstName(request.firstName())
                .middleName(request.middleName())
                .fullName(fullName)
                .age(request.age())
                .build();
        storage.patients.put(id, patient);
        return patient;
    }

    public PatientResponse update(Long id, PatientRequest request) {
        String fullName = request.firstName() + " " + request.lastName() + " " + request.middleName();
        PatientResponse updatedPatient = PatientResponse.builder()
            .id(id)
            .lastName(request.lastName())
            .firstName(request.firstName())
            .middleName(request.middleName())
            .fullName(fullName)
            .age(request.age())
            .build();
        storage.patients.put(id, updatedPatient);
        return updatedPatient;
    }

    public PatientResponse patchPatient(Long id, PatchPatientRequest request) {
        PatientResponse existing = findById(id);
        String newFirstName = request.firstName() != null ? request.firstName() : existing.getFirstName();
        String newLastName = request.lastName() != null ? request.lastName() : existing.getLastName();
        String newMiddleName = request.middleName() != null ? request.middleName() : existing.getMiddleName();
        String newFullName = newFirstName + " " + newLastName;
        PatientResponse updated = PatientResponse.builder()
                .id(id)
                .firstName(newFirstName)
                .lastName(newLastName)
                .middleName(newMiddleName)
                .fullName(newFullName)
                .age(request.age() != null ? request.age() : existing.getAge())
                .build();
        storage.patients.put(id, updated);
        return updated;
    }

    public PatientResponse delete(Long id) {
        findById(id);
        appointmentService.deleteAppointmentsByPatientId(id);
        storage.patients.remove(id);
        return PatientResponse.builder().id(id).build();
    }
}