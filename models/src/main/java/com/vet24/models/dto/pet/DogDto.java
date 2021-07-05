package com.vet24.models.dto.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Getter
@Setter
public class DogDto extends AbstractNewPetDto {

    private String avatar;

    private Integer notificationCount;

    public DogDto() {
        super();
    }

    @JsonCreator
    public DogDto(String name, PetType petType, LocalDate birthDay,
                  Gender gender, String breed, String color,
                  PetSize size, Double weight, String description,
                  String avatar, Integer notificationCount) {
        super(name, PetType.DOG, birthDay, gender, breed, color, size, weight, description);
        this.avatar = avatar;
        this.notificationCount = notificationCount;
    }
}
