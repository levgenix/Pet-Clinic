package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EchinococcusMapper extends AbstractProcedureMapper,
        DtoMapper<EchinococcusProcedure, EchinococcusDto>, EntityMapper<EchinococcusDto, EchinococcusProcedure> {

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    EchinococcusDto toDto(EchinococcusProcedure echinococcusProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    @Override
    EchinococcusProcedure toEntity(EchinococcusDto echinococcusDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    EchinococcusProcedure procedureDtoToEchinococcus(ProcedureDto procedureDto);

    @Override
    default ProcedureType getProcedureType() {
        return ProcedureType.ECHINOCOCCUS;
    }

    @Override
    default Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto abstractNewProcedureDto) {
        return toEntity((EchinococcusDto) abstractNewProcedureDto);
    }

    @Override
    default Procedure procedureDtoToProcedure(ProcedureDto procedureDto) {
        return procedureDtoToEchinococcus(procedureDto);
    }
}
