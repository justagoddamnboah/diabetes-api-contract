package edu.rutmiit.demo.demorest.service;

import edu.rutmiit.demo.diabetesapicontract.dto.*;
import edu.rutmiit.demo.diabetesapicontract.exception.AppointmentTimeAlreadyExistsException;
import edu.rutmiit.demo.diabetesapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppointmentService {

    private final InMemoryStorage storage;
    private final PatientService patientService;

    public AppointmentService(InMemoryStorage storage, @Lazy PatientService patientService) {
        this.storage = storage;
        this.patientService = patientService;
    }

    public AppointmentResponse findAppointmentById(Long id) {
        return Optional.ofNullable(storage.appointments.get(id))
            .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
    }

    public PagedResponse<AppointmentResponse> findAllAppointments(Long patientId, int page, int size) {
        Stream<AppointmentResponse> stream = storage.appointments.values().stream()
            .sorted(Comparator.comparing(AppointmentResponse::getId));

        if (patientId != null) {
            stream = stream.filter(a -> a.getPatient() != null && a.getPatient().getId().equals(patientId));
        }

        List<AppointmentResponse> allAppointments = stream.toList();
        int totalElements = allAppointments.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<AppointmentResponse> content = (from >= totalElements) ? List.of() : allAppointments.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public AppointmentResponse createAppointment(AppointmentRequest request) {
        validateAppointmentTime(request.appointmentTime(), null);
        PatientResponse patient = patientService.findById(request.patientId());

        long id = storage.appointmentSequence.incrementAndGet();

        AppointmentResponse appointment = AppointmentResponse.builder()
            .id(id)
            .patient(patient)
            .appointmentTime(request.appointmentTime())
            .bloodSugar(request.bloodSugar())
            .onDiet(request.onDiet())
            .fasting(request.fasting())
            .createdAt(LocalDateTime.now())
            .build();
        storage.appointments.put(id, appointment);
        patientService.recalculateAppsCount(patient.getId());
        return appointment;
    }

    public AppointmentResponse updateAppointment(Long id, UpdateAppointmentRequest request) {
        AppointmentResponse existing = findAppointmentById(id);
        validateAppointmentTime(request.appointmentTime(), id);

        AppointmentResponse updated = AppointmentResponse.builder()
            .id(id)
            .patient(existing.getPatient())
            .appointmentTime(request.appointmentTime())
            .bloodSugar(request.bloodSugar())
            .onDiet(request.onDiet())
            .fasting(request.fasting())
            .createdAt(existing.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
        storage.appointments.put(id, updated);
        return updated;
    }

    public AppointmentResponse patchAppointment(Long id, PatchAppointmentRequest request) {
        AppointmentResponse existing = findAppointmentById(id);

        if (request.appointmentTime() != null) {
            validateAppointmentTime(request.appointmentTime(), id);
        }

        AppointmentResponse updated = AppointmentResponse.builder()
            .id(id)
            .patient(existing.getPatient())
            .appointmentTime(request.appointmentTime() != null ? request.appointmentTime() : existing.getAppointmentTime())
            .bloodSugar(request.bloodSugar() != null ? request.bloodSugar() : existing.getBloodSugar())
            .onDiet(request.onDiet() != null ? request.onDiet() : existing.isOnDiet())
            .fasting(request.fasting() != null ? request.fasting() : existing.isFasting())
            .updatedAt(LocalDateTime.now())
            .createdAt(existing.getCreatedAt())
            .build();
        storage.appointments.put(id, updated);
        return updated;
    }

    public void deleteAppointment(Long id) {
        AppointmentResponse appointment = storage.appointments.get(id);
        Long patientId = appointment.getPatient().getId();
        findAppointmentById(id);
        storage.appointments.remove(id);
        patientService.recalculateAppsCount(patientId);
    }

    public void deleteAppointmentsByPatientId(Long patientId) {
        patientService.recalculateAppsCount(patientId);
        List<Long> toDelete = storage.appointments.values().stream()
            .filter(p -> p.getPatient() != null && p.getPatient().getId().equals(patientId))
            .map(AppointmentResponse::getId)
            .toList();
        toDelete.forEach(storage.appointments::remove);
    }

    private void validateAppointmentTime(String appointmentTime, Long currentAppointmentId) {
        storage.appointments.values().stream()
            .filter(a -> a.getAppointmentTime().equals(appointmentTime))
            .filter(a -> !a.getId().equals(currentAppointmentId))
            .findAny()
            .ifPresent(a -> {
                throw new AppointmentTimeAlreadyExistsException(appointmentTime);
            });
    }

    public List<AppSummaryResponse> getAppsSummary() {
        return storage.appointments.values().stream()
            .map(app -> new AppSummaryResponse(
                app.getId(),
                app.getPatient(),
                app.getAppointmentTime()
            ))
            .collect(Collectors.toList());
    }
}