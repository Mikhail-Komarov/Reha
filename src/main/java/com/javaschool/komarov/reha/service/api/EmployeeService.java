package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface EmployeeService {
    /**
     * Method to get employeeDTO by login
     *
     * @param login login
     * @return employeeDTO
     */
    EmployeeDto getEmployeeDTOByLogin(String login);

    /**
     * Method to get employee by login
     *
     * @param login login
     * @return employee
     */
    Optional<Employee> getEmployeeByLogin(String login);

    /**
     * Method to get employee by id
     *
     * @param id employee id
     * @return employee
     */
    Optional<Employee> getEmployeeById(Long id);

    /**
     * Method to check the presence of an employee in the database
     *
     * @param id employee id
     * @return boolean
     */
    boolean EmployeeIsExist(Long id);
}
