package edu.rutmiit.demo.events;

public sealed interface PatientEvent {

    record Created(
        Long patientId,
        String firstName,
        String lastName,
        String middleName,
        String fullName,
        Integer age
    ) implements PatientEvent {}

    record Deleted(
        Long patientId,
        String fullName,
        int deletedAppsCount
    ) implements PatientEvent {}
}