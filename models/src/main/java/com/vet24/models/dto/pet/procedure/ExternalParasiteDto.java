package com.vet24.models.dto.pet.procedure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.ProcedureType;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class ExternalParasiteDto extends AbstractNewProcedureDto {

    @JsonCreator
    public ExternalParasiteDto(LocalDate date, Long medicineId,
                               String medicineBatchNumber, Boolean isPeriodical, Integer periodDays) {
        super(date, ProcedureType.EXTERNAL_PARASITE, medicineId, medicineBatchNumber, isPeriodical, periodDays);
    }
}
