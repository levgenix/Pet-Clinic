package com.vet24.vaadin.models.pet;

import lombok.Data;

@Data
public class PetFound {

    private Long id;
    private String latitude;
    private String longitude;
    private String text;
}

