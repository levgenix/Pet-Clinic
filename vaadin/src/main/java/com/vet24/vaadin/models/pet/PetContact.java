package com.vet24.vaadin.models.pet;

import lombok.Data;

@Data
public class PetContact {

    private Long id;

    private String ownerName;

    private String address;

    private Long phone;

    private String petCode;

    private Pet pet;
}