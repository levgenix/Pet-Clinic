package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("VACCINATION")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class VaccinationProcedure extends Procedure {

    public VaccinationProcedure() {
        super();
    }

    public VaccinationProcedure(LocalDate date, String medicineBatchNumber, Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        super(date, ProcedureType.VACCINATION, medicineBatchNumber, isPeriodical, periodDays, medicine, pet);
    }
}
