package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface EmployeeService {
    EmployeeDto getEmployeeDTOByLogin(String login);

    Optional<Employee> getEmployeeByLogin(String login);

    Optional<Employee> getEmployeeById(Long id);

    boolean EmployeeIsExist(Long id);
}
