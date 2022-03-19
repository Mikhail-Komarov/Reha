package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.TherapyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapyDto {
    private Long id;
    private String name;
    private TherapyType therapyType;

}
