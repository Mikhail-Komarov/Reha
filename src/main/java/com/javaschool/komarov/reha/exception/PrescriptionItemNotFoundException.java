package com.javaschool.komarov.reha.exception;

public class PrescriptionItemNotFoundException extends RuntimeException {
    public PrescriptionItemNotFoundException() {
        super("Prescription item not found!");
    }
}
