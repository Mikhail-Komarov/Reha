package com.javaschool.komarov.reha.model.dto;

import com.javaschool.komarov.reha.model.PatientStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String healthInsurance;
    private PatientStatus Status;

}
