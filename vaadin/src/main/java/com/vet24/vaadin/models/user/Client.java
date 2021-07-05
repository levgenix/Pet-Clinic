package com.vet24.vaadin.models.user;

import com.vet24.vaadin.models.pet.Pet;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Client {

    private String firstname;

    private String lastname;

    private String email;

    private Set<Pet> pets = new HashSet<>();
}
