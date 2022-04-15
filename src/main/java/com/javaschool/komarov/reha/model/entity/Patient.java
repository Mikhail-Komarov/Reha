package com.javaschool.komarov.reha.model.entity;

import com.javaschool.komarov.reha.model.PatientStatus;
import lombok.AllArgsConstructor;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToMany(mappedBy = "patient")
    private Set<Prescription> prescriptions;

}
