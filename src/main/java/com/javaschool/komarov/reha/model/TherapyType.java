package com.javaschool.komarov.reha.model;

public enum TherapyType {
    PROCEDURE("procedure"),
    MEDICINE("medicine");

    private final String value;

    TherapyType(String value) {
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
