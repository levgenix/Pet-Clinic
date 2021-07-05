package com.vet24.vaadin.models.pet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.vet24.vaadin.models.enums.Gender;
import com.vet24.vaadin.models.enums.PetType;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Pet {
    private Long id;

    private String name;

    private String avatar;

    private String breed;

    private Gender gender;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDay;

    @JsonAlias({"type", "petType"})
    private PetType petType;
}
