package com.javaschool.komarov.reha.security;

import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.mapper.EmployeeMapper;
import com.javaschool.komarov.reha.repository.HospitalEmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("employeeDetailsServiceImpl")
public class EmployeeDetailsServiceImpl implements UserDetailsService {
    private final HospitalEmployeeRepo hospitalEmployeeRepo;
    private final EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EmployeeDto employeeDto = employeeMapper.toDTO(hospitalEmployeeRepo.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("User does not exists")));
        return SecurityEmployee.fromEmployee(employeeDto);
    }
}
