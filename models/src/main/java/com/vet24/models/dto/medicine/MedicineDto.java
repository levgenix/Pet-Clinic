package com.vet24.models.dto.medicine;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MedicineDto {
    Long id;
    @NotBlank( message = "поле manufactureName не должно быть пустым")
    String manufactureName;
    @NotBlank( message = "поле name не должно быть пустым")
    String name;
    String icon;
    @NotBlank( message = "поле description не должно быть пустым")
    String description;

}
