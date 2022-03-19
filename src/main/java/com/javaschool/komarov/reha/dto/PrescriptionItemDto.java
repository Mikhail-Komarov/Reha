package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemDto {
    private Long itemId;
    private Integer dose;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTreatment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTreatment;
    private String timePattern;
    @DateTimeFormat(pattern = "dd.MM.yy")
    private List<LocalDate> date;
    @DateTimeFormat(pattern = "H:mm")
    private List<LocalTime> time;
    private PrescriptionItemStatus prescriptionItemStatus;
    private String cancellationReason;
    private EmployeeDto employee;
    private TherapyDto therapy;
    private PrescriptionDto prescription;
    private long employeeId;
    private long therapyId;
    private long prescriptionId;

}
