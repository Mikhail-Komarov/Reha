package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.model.Therapy;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-05T19:36:29+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class TherapyMapperImpl implements TherapyMapper {

    @Override
    public TherapyDto toDTO(Therapy model) {
        if ( model == null ) {
            return null;
        }

        TherapyDto therapyDto = new TherapyDto();

        therapyDto.setId( model.getId() );
        therapyDto.setName( model.getName() );
        therapyDto.setTherapyType( model.getTherapyType() );

        return therapyDto;
    }

    @Override
    public Therapy toModel(TherapyDto dto) {
        if ( dto == null ) {
            return null;
        }

        Therapy therapy = new Therapy();

        therapy.setId( dto.getId() );
        therapy.setName( dto.getName() );
        therapy.setTherapyType( dto.getTherapyType() );

        return therapy;
    }
}
