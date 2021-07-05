package com.vet24.vaadin.models.pet.reproduction;

import com.vet24.vaadin.models.pet.Pet;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Reproduction {

    private Long id;

    private LocalDate estrusStart;

    private LocalDate mating;

    private LocalDate dueDate;

    private Integer childCount;

    private Pet pet;
}
