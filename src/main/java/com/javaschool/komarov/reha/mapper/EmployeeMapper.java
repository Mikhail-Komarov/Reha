package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.entity.Employee;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {
    EmployeeDto toDTO(Employee model);

    Employee toModel(EmployeeDto dto);
}
