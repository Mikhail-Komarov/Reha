package com.javaschool.komarov.reha.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    private Long prescriptionId;
    private String diagnosis;
    private String doctorName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

}
