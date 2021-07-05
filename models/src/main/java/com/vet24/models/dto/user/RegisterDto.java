package com.vet24.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
public class RegisterDto {

    @Email(message = "{registration.validation.email}")
    private String email;

    @NotBlank(message = "{registration.validation.blank.field}")
    private String firstname;

    @NotBlank(message = "{registration.validation.blank.field} ")
    private String lastname;

    @NotBlank(message = "{registration.validation.blank.field}")
    @Size(min=5,max=15)
    private String password;

    @NotBlank(message = "{registration.validation.blank.field}")
    @Size(min=5,max=15)
    private String confirmPassword;

}
