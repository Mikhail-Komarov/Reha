package com.javaschool.komarov.reha.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private String diagnosis;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne
    private Employee employee;
    @OneToMany(mappedBy = "prescription")
    private Set<PrescriptionItem> prescriptionItems;

    public Prescription(Long id, String diagnosis, Patient patient, Employee employee, Set<PrescriptionItem> prescriptionItems) {
        this.id = id;
        this.diagnosis = diagnosis;
        this.patient = patient;
        this.employee = employee;
        this.prescriptionItems = prescriptionItems;
    }

    public Prescription(String diagnosis, Patient patient, Employee employee, Set<PrescriptionItem> prescriptionItems) {
        this.diagnosis = diagnosis;
        this.patient = patient;
        this.employee = employee;
        this.prescriptionItems = prescriptionItems;
    }

    public Prescription(String diagnosis, Patient patient, Employee employee) {
        this.diagnosis = diagnosis;
        this.patient = patient;
        this.employee = employee;
    }
}
