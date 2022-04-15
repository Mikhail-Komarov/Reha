package com.javaschool.komarov.reha.model;

public enum PrescriptionItemStatus {
    PRESCRIBED("prescribed"),
    CANCELLED("cancelled");

    private final String value;

    PrescriptionItemStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
    }
