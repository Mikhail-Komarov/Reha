package com.javaschool.komarov.reha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    private Long id;
    private String diagnosis;
    private PatientDto patient;
    private EmployeeDto employee;

    public PrescriptionDto(String diagnosis, PatientDto patient, EmployeeDto employee) {
        this.diagnosis = diagnosis;
        this.patient = patient;
        this.employee = employee;
    }
}
