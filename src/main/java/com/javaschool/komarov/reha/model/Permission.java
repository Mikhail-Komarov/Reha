package com.javaschool.komarov.reha.model;

public enum Permission {
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_ADD("employee:add"),
    EMPLOYEE_WRITE("employee:write");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
