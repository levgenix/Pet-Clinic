package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ECHINOCOCCUS")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class EchinococcusProcedure extends Procedure {

    public EchinococcusProcedure() {
        super();
    }

    public EchinococcusProcedure(LocalDate date, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        super(date, ProcedureType.ECHINOCOCCUS, medicineBatchNumber, isPeriodical, periodDays, medicine, pet);
    }
}
