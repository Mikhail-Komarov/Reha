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
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    private String cancellationReason;
    private LocalDateTime dateTime;
    @ManyToOne
    private PrescriptionItem prescriptionItem;

    public Event(Long id, EventStatus eventStatus, String cancellationReason, LocalDateTime dateTime, PrescriptionItem prescriptionItem) {
        this.id = id;
        this.eventStatus = eventStatus;
        this.cancellationReason = cancellationReason;
        this.dateTime = dateTime;
        this.prescriptionItem = prescriptionItem;
    }

    public Event(EventStatus eventStatus, String cancellationReason, LocalDateTime dateTime, PrescriptionItem prescriptionItem) {
        this.eventStatus = eventStatus;
        this.cancellationReason = cancellationReason;
        this.dateTime = dateTime;
        this.prescriptionItem = prescriptionItem;
    }

    public Event(EventStatus eventStatus, LocalDateTime dateTime, PrescriptionItem prescriptionItem) {
        this.eventStatus = eventStatus;
        this.dateTime = dateTime;
        this.prescriptionItem = prescriptionItem;
    }
}
