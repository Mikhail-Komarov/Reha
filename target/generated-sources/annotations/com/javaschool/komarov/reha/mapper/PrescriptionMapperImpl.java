package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.Employee;
import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.model.Prescription;
import java.util.ArrayList;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-05T19:36:29+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class PrescriptionMapperImpl implements PrescriptionMapper {

    @Override
    public PrescriptionDto toDTO(Prescription model) {
        if ( model == null ) {
            return null;
        }

        PrescriptionDto prescriptionDto = new PrescriptionDto();

        prescriptionDto.setId( model.getId() );
        prescriptionDto.setDiagnosis( model.getDiagnosis() );
        prescriptionDto.setPatient( patientToPatientDto( model.getPatient() ) );
        prescriptionDto.setEmployee( employeeToEmployeeDto( model.getEmployee() ) );

        return prescriptionDto;
    }

    @Override
    public Prescription toModel(PrescriptionDto dto) {
        if ( dto == null ) {
            return null;
        }

        Prescription prescription = new Prescription();

        prescription.setId( dto.getId() );
        prescription.setDiagnosis( dto.getDiagnosis() );
        prescription.setPatient( patientDtoToPatient( dto.getPatient() ) );
        prescription.setEmployee( employeeDtoToEmployee( dto.getEmployee() ) );

        return prescription;
    }

    @Override
    public Iterable<PrescriptionDto> toDTOList(Iterable<Prescription> models) {
        if ( models == null ) {
            return null;
        }

        ArrayList<PrescriptionDto> iterable = new ArrayList<PrescriptionDto>();
        for ( Prescription prescription : models ) {
            iterable.add( toDTO( prescription ) );
        }

        return iterable;
    }

    protected PatientDto patientToPatientDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientDto patientDto = new PatientDto();

        patientDto.setId( patient.getId() );
        patientDto.setFirstName( patient.getFirstName() );
        patientDto.setLastName( patient.getLastName() );
        patientDto.setHealthInsurance( patient.getHealthInsurance() );
        patientDto.setStatus( patient.getStatus() );

        return patientDto;
    }

    protected EmployeeDto employeeToEmployeeDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId( employee.getId() );
        employeeDto.setFirstName( employee.getFirstName() );
        employeeDto.setLastName( employee.getLastName() );
        employeeDto.setLogin( employee.getLogin() );
        employeeDto.setRole( employee.getRole() );

        return employeeDto;
    }

    protected Patient patientDtoToPatient(PatientDto patientDto) {
        if ( patientDto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setId( patientDto.getId() );
        patient.setFirstName( patientDto.getFirstName() );
        patient.setLastName( patientDto.getLastName() );
        patient.setHealthInsurance( patientDto.getHealthInsurance() );
        patient.setStatus( patientDto.getStatus() );

        return patient;
    }

    protected Employee employeeDtoToEmployee(EmployeeDto employeeDto) {
        if ( employeeDto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( employeeDto.getId() );
        employee.setFirstName( employeeDto.getFirstName() );
        employee.setLastName( employeeDto.getLastName() );
        employee.setLogin( employeeDto.getLogin() );
        employee.setRole( employeeDto.getRole() );

        return employee;
    }
}
