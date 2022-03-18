package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.mapper.EmployeeMapper;
import com.javaschool.komarov.reha.repository.HospitalEmployeeRepo;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final HospitalEmployeeRepo employeeRepo;

    public EmployeeService(EmployeeMapper employeeMapper, HospitalEmployeeRepo employeeRepo) {
        this.employeeMapper = employeeMapper;
        this.employeeRepo = employeeRepo;
    }

    public EmployeeDto getEmployeeByLogin(String login) {
        if (employeeRepo.findByLogin(login).isPresent()) {
            return employeeMapper.toDTO(employeeRepo.findByLogin(login).get());
        } else {
            return null;
        }
    }

    public EmployeeDto getEmployeeDtoById(Long id) {
        if (employeeRepo.findById(id).isPresent()) {
            return employeeMapper.toDTO(employeeRepo.findById(id).get());
        } else {
            return null;
        }
    }
}
