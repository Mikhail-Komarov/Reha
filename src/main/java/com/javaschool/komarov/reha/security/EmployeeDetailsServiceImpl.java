package com.javaschool.komarov.reha.security;

import com.javaschool.komarov.reha.model.Employee;
import com.javaschool.komarov.reha.repository.HospitalEmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("employeeDetailsServiceImpl")
public class EmployeeDetailsServiceImpl implements UserDetailsService {
    private final HospitalEmployeeRepo hospitalEmployeeRepo;

    @Autowired
    public EmployeeDetailsServiceImpl(HospitalEmployeeRepo hospitalEmployeeRepo) {
        this.hospitalEmployeeRepo = hospitalEmployeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Employee employee = hospitalEmployeeRepo.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return SecurityEmployee.fromEmployee(employee);
    }
}
