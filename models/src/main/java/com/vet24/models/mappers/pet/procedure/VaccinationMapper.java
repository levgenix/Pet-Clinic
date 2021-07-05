package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccinationMapper extends AbstractProcedureMapper,
        DtoMapper<VaccinationProcedure, VaccinationDto>, EntityMapper<VaccinationDto, VaccinationProcedure> {

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    VaccinationDto toDto(VaccinationProcedure vaccinationProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    @Override
    VaccinationProcedure toEntity(VaccinationDto vaccinationDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    VaccinationProcedure procedureDtoToVaccination(ProcedureDto procedureDto);

    @Override
    default ProcedureType getProcedureType() {
        return ProcedureType.VACCINATION;
    }

    @Override
    default Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto abstractNewProcedureDto) {
        return toEntity((VaccinationDto) abstractNewProcedureDto);
    }

    @Override
    default Procedure procedureDtoToProcedure(ProcedureDto procedureDto) {
        return procedureDtoToVaccination(procedureDto);
    }
}
