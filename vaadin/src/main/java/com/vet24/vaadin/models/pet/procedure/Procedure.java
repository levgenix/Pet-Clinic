package com.vet24.vaadin.models.pet.procedure;

import com.vet24.vaadin.models.enums.ProcedureType;
import com.vet24.vaadin.models.medicine.Medicine;
import com.vet24.vaadin.models.pet.Pet;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Procedure {

    private Long id;

    private LocalDate date;

    public ProcedureType type;

    private String medicineBatchNumber;

    private Boolean isPeriodical;

    private Integer periodDays;

    private Medicine medicine;

    private Pet pet;
}
