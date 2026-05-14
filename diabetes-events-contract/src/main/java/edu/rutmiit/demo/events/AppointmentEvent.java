package edu.rutmiit.demo.events;

public sealed interface AppointmentEvent {

    record Created(
            Long appId,
            String appointmentTime,
            Float bloodSugar,
            Long patientId,
            String patientFullName,
            Boolean fasting,
            Boolean onDiet
    ) implements AppointmentEvent {}

    record Updated(
            Long appId,
            String appointmentTime,
            Float bloodSugar,
            Boolean fasting,
            Boolean onDiet
    ) implements AppointmentEvent {}

    record Deleted(
            Long appId,
            String appointmentTime
    ) implements AppointmentEvent {}
}