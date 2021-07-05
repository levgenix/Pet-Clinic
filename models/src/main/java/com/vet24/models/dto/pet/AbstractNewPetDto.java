package com.vet24.models.dto.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "petType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DogDto.class, name = "DOG"),
        @JsonSubTypes.Type(value = CatDto.class, name = "CAT")
})
public abstract class AbstractNewPetDto {
    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;
    private PetType petType;
    @PastOrPresent(message = "Поле birthDate должно содержать прошедшую дату")
    private LocalDate birthDay;
    private Gender gender; //male, female
    @NotBlank(message = "Поле breed не должно быть пустым")
    private String breed;
    @NotBlank(message = "Поле color не должно быть пустым")
    private String color;
    private PetSize size; //small, medium, big
    @Positive(message = "Поле weight должно быть больше 0")
    private Double weight; // кг, округляем до десятых - 10,1, 12,5
    private String description;

    @JsonCreator
    protected AbstractNewPetDto(String name, PetType petType, LocalDate birthDay,
                                Gender gender, String breed, String color,
                                PetSize size, Double weight, String description) {
        this.name = name;
        this.petType = petType;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.color = color;
        this.size = size;
        this.weight = weight;
        this.description = description;
    }

    protected AbstractNewPetDto() {

    }
}
