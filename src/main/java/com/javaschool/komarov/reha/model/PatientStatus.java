package com.javaschool.komarov.reha.model;

public enum PatientStatus {
    UNDEFINED("under consideration"),
    IS_TREATED("is treated"),
    DISCHARGED("discharged");

    private final String value;

    PatientStatus(String value) {
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

