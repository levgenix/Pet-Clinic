package com.vet24.models.dto.medicine;

import com.vet24.models.dto.pet.procedure.ProcedureDto;
import lombok.Data;

import java.util.List;

@Data
public class TreatmentDto {
     Long id;
     Long diagnosisId;
     List<ProcedureDto> procedureDtoList;
     Long doctorId;
}
