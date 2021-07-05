package com.vet24.vaadin.models.medicine;

import lombok.Data;

@Data
public class Medicine {

    private Long id;

    private String manufactureName;

    private String name;

    private String icon;

    private String description;
}
