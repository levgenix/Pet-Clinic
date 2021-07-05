package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.TreatmentDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.mappers.pet.procedure.ProcedureMapperImpl;
import com.vet24.models.medicine.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProcedureMapperImpl.class})
public interface TreatmentMapper extends DtoMapper<Treatment,TreatmentDto>, EntityMapper<TreatmentDto, Treatment> {

    @Override
    @Mapping(source = "diagnosis.id", target = "diagnosisId")
    @Mapping(source = "diagnosis.doctor.id", target = "doctorId")
    @Mapping(source = "procedureList", target = "procedureDtoList")
    TreatmentDto toDto(Treatment treatment);
}