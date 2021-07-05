package com.vet24.models.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetFoundDto {

    private String latitude;
    private String longitude;
    private String text;
}
