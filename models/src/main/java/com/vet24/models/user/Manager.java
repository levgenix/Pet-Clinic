package com.vet24.models.user;

import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MANAGER")
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {

    public Manager(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }

    public Manager() {

    }
}
