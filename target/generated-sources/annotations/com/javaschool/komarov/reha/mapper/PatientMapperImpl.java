package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.model.Patient;
import java.util.ArrayList;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-18T15:36:14+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public PatientDto toDTO(Patient model) {
        if ( model == null ) {
            return null;
        }

        PatientDto patientDto = new PatientDto();

        patientDto.setId( model.getId() );
        patientDto.setFirstName( model.getFirstName() );
        patientDto.setLastName( model.getLastName() );
        patientDto.setHealthInsurance( model.getHealthInsurance() );
        patientDto.setStatus( model.getStatus() );

        return patientDto;
    }

    @Override
    public Patient toModel(PatientDto dto) {
        if ( dto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setId( dto.getId() );
        patient.setFirstName( dto.getFirstName() );
        patient.setLastName( dto.getLastName() );
        patient.setHealthInsurance( dto.getHealthInsurance() );
        patient.setStatus( dto.getStatus() );

        return patient;
    }

    @Override
    public Iterable<Patient> toModelList(Iterable<PatientDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        ArrayList<Patient> iterable = new ArrayList<Patient>();
        for ( PatientDto patientDto : dtos ) {
            iterable.add( toModel( patientDto ) );
        }

        return iterable;
    }

    @Override
    public Iterable<PatientDto> toDTOList(Iterable<Patient> models) {
        if ( models == null ) {
            return null;
        }

        ArrayList<PatientDto> iterable = new ArrayList<PatientDto>();
        for ( Patient patient : models ) {
            iterable.add( toDTO( patient ) );
        }

        return iterable;
    }
}
