package com.vet24.vaadin.models.user;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String avatar;

    private Boolean enabled;

    private Role role;
}
