package com.javaschool.komarov.reha.model.entity;

import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prescriptionItem")
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private Integer dose;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTreatment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

}
