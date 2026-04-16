package edu.rutmiit.demo.demorest.storage;

import edu.rutmiit.demo.diabetesapicontract.dto.AppointmentResponse;
import edu.rutmiit.demo.diabetesapicontract.dto.PatientResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, PatientResponse> patients = new ConcurrentHashMap<>();
    public final Map<Long, AppointmentResponse> appointments = new ConcurrentHashMap<>();

    public final AtomicLong patientSequence = new AtomicLong(0);
    public final AtomicLong appointmentSequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        PatientResponse patient1 = PatientResponse.builder()
            .id(patientSequence.incrementAndGet())
            .lastName("Горчаков")
            .firstName("Иван")
            .middleName("Андреевич")
            .fullName("Горчаков Иван Андреевич")
            .age(32)
            .appCount(1)
            .build();

        patients.put(patient1.getId(), patient1);

        PatientResponse patient2 = PatientResponse.builder()
            .id(patientSequence.incrementAndGet())
            .lastName("Петров")
            .firstName("Петр")
            .middleName("Петрович")
            .fullName("Петров Петр Петрович")
            .age(45)
            .appCount(0)
            .build();

        patients.put(patient2.getId(), patient2);

        long appId1 = appointmentSequence.incrementAndGet();
        appointments.put(appId1, AppointmentResponse.builder()
            .id(appId1)
            .patient(patient1)
            .appointmentTime("2025-06-17 12:00:00")
            .bloodSugar(5.5f)
            .onDiet(false)
            .fasting(true)
            .createdAt(LocalDateTime.now())
            .build());
    }
}