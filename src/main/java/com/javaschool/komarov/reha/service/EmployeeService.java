package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.mapper.EmployeeMapper;
import com.javaschool.komarov.reha.repository.HospitalEmployeeRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final HospitalEmployeeRepo employeeRepo;

    public EmployeeService(EmployeeMapper employeeMapper, HospitalEmployeeRepo employeeRepo) {
        this.employeeMapper = employeeMapper;
        this.employeeRepo = employeeRepo;
    }

    public Optional<EmployeeDto> getEmployeeByLogin(String login) {
        return Optional.of(employeeMapper.toDTO(employeeRepo.findByLogin(login).get()));
    }

    public EmployeeDto getEmployeeDtoById(Long id) {
        return employeeMapper.toDTO(employeeRepo.findById(id).get());
    }
}
