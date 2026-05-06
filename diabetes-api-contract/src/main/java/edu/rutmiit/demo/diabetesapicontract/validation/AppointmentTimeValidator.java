package edu.rutmiit.demo.diabetesapicontract.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class AppointmentTimeValidator implements ConstraintValidator<ValidAppointmentTime, String> {

    private static final DateTimeFormatter STRICT_FORMATTER = DateTimeFormatter
        .ofPattern("uuuu-MM-dd HH:mm:ss")
        .withResolverStyle(ResolverStyle.STRICT);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            LocalDateTime.parse(value, STRICT_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}