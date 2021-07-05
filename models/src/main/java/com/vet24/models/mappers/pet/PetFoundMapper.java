package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.PetFound;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetFoundMapper extends DtoMapper<PetFound, PetFoundDto>, EntityMapper<PetFoundDto, PetFound> {

}
