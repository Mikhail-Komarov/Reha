package com.javaschool.komarov.reha.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(name = "healthInsurance", unique = true, nullable = false)
    private String healthInsurance;
    @Enumerated(EnumType.STRING)
    private PatientStatus Status;
    @OneToMany(mappedBy = "patient",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Prescription> prescriptions;

    public Patient(String firstName, String lastName, String healthInsurance, PatientStatus status, Set<Prescription> prescriptions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthInsurance = healthInsurance;
        Status = status;
        this.prescriptions = prescriptions;
    }

    public Patient(String firstName, String lastName, String healthInsurance, PatientStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthInsurance = healthInsurance;
        Status = status;
    }

    public Patient(String firstName, String lastName, String healthInsurance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthInsurance = healthInsurance;
    }

    public Patient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(String healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public PatientStatus getStatus() {
        return Status;
    }

    public void setStatus(PatientStatus status) {
        Status = status;
    }

    public Set<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
