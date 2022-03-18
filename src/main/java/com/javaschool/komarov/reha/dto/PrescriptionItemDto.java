package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemDto {
    private Long id;
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

    public PrescriptionItemDto(Integer dose, LocalDate startTreatment, LocalDate endTreatment, String timePattern, PrescriptionItemStatus prescriptionItemStatus,
                               String cancellationReason, EmployeeDto employee, TherapyDto therapy, PrescriptionDto prescription) {
        this.dose = dose;
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescriptionItemStatus = prescriptionItemStatus;
        this.cancellationReason = cancellationReason;
        this.employee = employee;
        this.therapy = therapy;
        this.prescription = prescription;
    }

    public PrescriptionItemDto(LocalDate startTreatment, LocalDate endTreatment, String timePattern, PrescriptionItemStatus prescriptionItemStatus,
                               String cancellationReason, EmployeeDto employee, TherapyDto therapy, PrescriptionDto prescription) {
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescriptionItemStatus = prescriptionItemStatus;
        this.cancellationReason = cancellationReason;
        this.employee = employee;
        this.therapy = therapy;
        this.prescription = prescription;
    }

    public PrescriptionItemDto(LocalDate startTreatment, LocalDate endTreatment, String timePattern, PrescriptionItemStatus prescriptionItemStatus,
                               EmployeeDto employee, TherapyDto therapy, PrescriptionDto prescription) {
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescriptionItemStatus = prescriptionItemStatus;
        this.employee = employee;
        this.therapy = therapy;
        this.prescription = prescription;
    }
}
