package com.vet24.models.dto.user;

import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.user.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class ClientDto {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    private String avatar;

    @Email
    private String email;

    private List<PetDto> pets;
}
