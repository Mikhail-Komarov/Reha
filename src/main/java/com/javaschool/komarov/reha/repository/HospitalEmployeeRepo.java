package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.Employee;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface HospitalEmployeeRepo extends CrudRepository<Employee, Long> {

    Optional<Employee> findByLogin(String login);
}
