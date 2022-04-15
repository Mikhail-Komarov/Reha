package com.javaschool.komarov.reha.model;

public enum EventStatus {
    SCHEDULED("scheduled"),
    CANCELLED("cancelled"),
    COMPLETED("completed");

    private final String value;

    EventStatus(String value) {
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
