package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.TherapyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TherapyDto {
    private Long id;
    private String name;
    private TherapyType therapyType;

}
