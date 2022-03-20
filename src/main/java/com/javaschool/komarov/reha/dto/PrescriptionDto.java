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
    private Long prescriptionId;
    private String diagnosis;
    private PatientDto patient;
    private EmployeeDto employee;

}
