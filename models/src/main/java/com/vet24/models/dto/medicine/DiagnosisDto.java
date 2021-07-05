package com.vet24.models.dto.medicine;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DiagnosisDto {
    private Long id;
    private Long petId;
    private Long doctorId;
    private String description;
}
