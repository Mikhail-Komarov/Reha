package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.mapper.EmployeeMapper;
import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.entity.Employee;
import com.javaschool.komarov.reha.repository.HospitalEmployeeRepo;
import com.javaschool.komarov.reha.service.api.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final HospitalEmployeeRepo employeeRepo;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, HospitalEmployeeRepo employeeRepo) {
        this.employeeMapper = employeeMapper;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EmployeeDto getEmployeeDTOByLogin(String login) {
        Optional<Employee> employee = employeeRepo.findByLogin(login);
        return employee.map(employeeMapper::toDTO).orElseThrow(RuntimeException::new);
    }

    @Override
    public Optional<Employee> getEmployeeByLogin(String login) {
        return employeeRepo.findByLogin(login);
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepo.findById(id);
    }

    @Override
    public boolean EmployeeIsExist(Long id) {
        return employeeRepo.findById(id).isPresent();
    }
}
