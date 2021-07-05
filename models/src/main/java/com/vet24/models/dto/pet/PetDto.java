package com.vet24.models.dto.pet;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.PetType;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class PetDto {
    private Long id;
    private String name;

    private String avatar;
    private LocalDate birthDay;

    private Integer notificationCount;

    private PetType type; //dog, cat (в будущем добавим еще видов питомцев)
}
