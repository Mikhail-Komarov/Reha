package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.PatientStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String healthInsurance;
    private PatientStatus Status;

    public PatientDto(Long id, String firstName, String lastName, String healthInsurance, PatientStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthInsurance = healthInsurance;
        Status = status;
    }

    public PatientDto(String firstName, String lastName, String healthInsurance, PatientStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthInsurance = healthInsurance;
        Status = status;
    }
}
