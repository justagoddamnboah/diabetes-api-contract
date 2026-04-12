package edu.rutmiit.demo.diabetesapicontract.exception;

public class AppointmentTimeAlreadyExistsException extends RuntimeException {
    public AppointmentTimeAlreadyExistsException(String appointmentTime) {
        super("Appointment with appointmentTime=" + appointmentTime + " already exists");
    }
}