package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.PatientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String healthInsurance;
    private PatientStatus Status;

}
