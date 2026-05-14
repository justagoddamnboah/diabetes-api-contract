package edu.rutmiit.demo.events;

public final class RoutingKeys {

    private RoutingKeys() {

    }

    public static final String EXCHANGE = "appointments.events";

    public static final String APPOINTMENT_CREATED = "appointment.created";
    public static final String APPOINTMENT_UPDATED = "appointment.updated";
    public static final String APPOINTMENT_DELETED = "appointment.deleted";

    public static final String PATIENT_CREATED = "patient.created";
    public static final String PATIENT_DELETED = "patient.deleted";

    public static final String ALL_APPOINTMENT_EVENTS = "appointment.*";
    public static final String ALL_PATIENT_EVENTS = "patient.*";
    public static final String ALL_EVENTS = "#";
}