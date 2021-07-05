package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.medicine.Diagnosis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper extends EntityMapper<DiagnosisDto, Diagnosis> {

    @Mapping(source="pet.id", target="petId")
    @Mapping(source="doctor.id", target="doctorId")
    @Override
    DiagnosisDto toDto(Diagnosis diagnosis);
}
