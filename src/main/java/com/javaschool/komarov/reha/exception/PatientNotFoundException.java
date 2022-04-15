package com.javaschool.komarov.reha.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException() {
        super("Patient not found!");
    }
}
