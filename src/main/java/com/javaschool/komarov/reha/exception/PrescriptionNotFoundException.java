package com.javaschool.komarov.reha.exception;

public class PrescriptionNotFoundException extends RuntimeException {
    public PrescriptionNotFoundException() {
        super("Prescription not found!");
    }
}
