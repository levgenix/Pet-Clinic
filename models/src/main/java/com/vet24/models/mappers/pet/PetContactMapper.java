package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.PetContact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetContactMapper extends DtoMapper<PetContact, PetContactDto>, EntityMapper<PetContactDto, PetContact> {

}

