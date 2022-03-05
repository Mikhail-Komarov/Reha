package com.javaschool.komarov.reha.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "prescriptionItem")
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private int dose;
    private LocalDate startTreatment;
    private LocalDate endTreatment;
    private String timePattern;
    @ManyToOne
    private Prescription prescription;
    @OneToMany(mappedBy = "prescriptionItem")
    private Set<Event> events;
    @ManyToOne
    private Therapy therapy;
    @ManyToOne
    private Employee employee;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PrescriptionItemStatus prescriptionItemStatus;
    private String cancellationReason;

    public PrescriptionItem(Long id, int dose, LocalDate startTreatment, LocalDate endTreatment, String timePattern, Prescription prescription, Set<Event> events,
                            Therapy therapy, Employee employee, PrescriptionItemStatus prescriptionItemStatus, String cancellationReason) {
        this.id = id;
        this.dose = dose;
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescription = prescription;
        this.events = events;
        this.therapy = therapy;
        this.employee = employee;
        this.prescriptionItemStatus = prescriptionItemStatus;
        this.cancellationReason = cancellationReason;
    }

    public PrescriptionItem(int dose, LocalDate startTreatment, LocalDate endTreatment, String timePattern, Prescription prescription, Set<Event> events,
                            Therapy therapy, Employee employee, PrescriptionItemStatus prescriptionItemStatus, String cancellationReason) {
        this.dose = dose;
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescription = prescription;
        this.events = events;
        this.therapy = therapy;
        this.employee = employee;
        this.prescriptionItemStatus = prescriptionItemStatus;
        this.cancellationReason = cancellationReason;
    }

    public PrescriptionItem(LocalDate startTreatment, LocalDate endTreatment, String timePattern, Prescription prescription, Set<Event> events, Therapy therapy,
                            Employee employee, PrescriptionItemStatus prescriptionItemStatus, String cancellationReason) {
        this.startTreatment = startTreatment;
        this.endTreatment = endTreatment;
        this.timePattern = timePattern;
        this.prescription = prescription;
        this.events = events;
        this.therapy = therapy;
        this.employee = employee;
        this.prescriptionItemStatus = prescriptionItemStatus;
        this.cancellationReason = cancellationReason;
    }

}
