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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "therapy")
public class Therapy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private TherapyType therapyType;
    @OneToMany(mappedBy = "therapy")
    private Set<PrescriptionItem> prescriptionItems;

    public Therapy(Long id, String name, TherapyType therapyType, Set<PrescriptionItem> prescriptionItems) {
        this.id = id;
        this.name = name;
        this.therapyType = therapyType;
        this.prescriptionItems = prescriptionItems;
    }

    public Therapy(String name, TherapyType therapyType, Set<PrescriptionItem> prescriptionItems) {
        this.name = name;
        this.therapyType = therapyType;
        this.prescriptionItems = prescriptionItems;
    }

    public Therapy(String name, TherapyType therapyType) {
        this.name = name;
        this.therapyType = therapyType;
    }
}

