package com.vet24.models.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class PetContactDto {
    @NotBlank(message = "Поле ownerName не должно быть пустым")
    private String ownerName;

    @NotBlank(message = "Поле address не должно быть пустым")
    private String address;

    @NotNull(message = "Поле phone не должно быть null")
    private Long phone;
}

