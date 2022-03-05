package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.TherapyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class TherapyDto {
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TherapyType therapyType;

    public TherapyDto(Long id, String name, TherapyType therapyType) {
        this.id = id;
        this.name = name;
        this.therapyType = therapyType;
    }

    public TherapyDto(String name, TherapyType therapyType) {
        this.name = name;
        this.therapyType = therapyType;
    }
}
