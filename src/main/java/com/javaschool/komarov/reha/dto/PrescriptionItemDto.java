package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionItemDto {
    private Long id;
    private int dose;
    private LocalDate startTreatment;
    private LocalDate endTreatment;
    private String timePattern;
    private PrescriptionItemStatus prescriptionItemStatus;
    private String cancellationReason;
    private EmployeeDto employee;
    private TherapyDto therapy;
    private PrescriptionDto prescription;

    public PrescriptionItemDto(Long id, int dose, LocalDate startTreatment, LocalDate endTreatment, String timePattern, PrescriptionItemStatus prescriptionItemStatus,
                               String cancellationReason, EmployeeDto employee, TherapyDto therapy, PrescriptionDto prescription) {
        this.id = id;
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

    public PrescriptionItemDto(int dose, LocalDate startTreatment, LocalDate endTreatment, String timePattern, PrescriptionItemStatus prescriptionItemStatus,
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

    public PrescriptionItemDto(int dose, LocalDate startTreatment, LocalDate endTreatment, String timePattern, PrescriptionItemStatus prescriptionItemStatus,
                               EmployeeDto employee, TherapyDto therapy, PrescriptionDto prescription) {
        this.dose = dose;
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescriptionItemStatus = prescriptionItemStatus;
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
}
