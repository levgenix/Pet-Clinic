package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("EXTERNAL_PARASITE")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class ExternalParasiteProcedure extends Procedure {

    public ExternalParasiteProcedure() {
        super();
    }

    public ExternalParasiteProcedure(LocalDate date, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        super(date, ProcedureType.EXTERNAL_PARASITE, medicineBatchNumber, isPeriodical, periodDays, medicine, pet);
    }
}
