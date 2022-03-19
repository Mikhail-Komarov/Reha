package com.javaschool.komarov.reha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    private Long prescriptionId;
    private String diagnosis;
    private PatientDto patient;
    private EmployeeDto employee;

}
