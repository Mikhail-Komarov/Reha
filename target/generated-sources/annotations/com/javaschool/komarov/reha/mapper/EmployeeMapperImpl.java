package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.Employee;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-19T18:26:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDto toDTO(Employee model) {
        if ( model == null ) {
            return null;
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId( model.getId() );
        employeeDto.setFirstName( model.getFirstName() );
        employeeDto.setLastName( model.getLastName() );
        employeeDto.setLogin( model.getLogin() );
        employeeDto.setPassword( model.getPassword() );
        employeeDto.setRole( model.getRole() );

        return employeeDto;
    }

    @Override
    public Employee toModel(EmployeeDto dto) {
        if ( dto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( dto.getId() );
        employee.setFirstName( dto.getFirstName() );
        employee.setLastName( dto.getLastName() );
        employee.setLogin( dto.getLogin() );
        employee.setPassword( dto.getPassword() );
        employee.setRole( dto.getRole() );

        return employee;
    }
}
