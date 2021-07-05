package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.pet.procedure.Procedure;

public interface AbstractProcedureMapper {
    ProcedureType getProcedureType();
    Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto abstractNewProcedureDto);
    Procedure procedureDtoToProcedure(ProcedureDto procedureDto);
}
