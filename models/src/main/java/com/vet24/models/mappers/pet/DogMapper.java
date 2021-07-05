package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DogMapper extends AbstractPetMapper, DtoMapper<Dog, DogDto>, EntityMapper<DogDto, Dog> {

    @Override
    default PetType getPetType() {
        return PetType.DOG;
    }

    @Override
    default Pet abstractNewPetDtoToPet(AbstractNewPetDto petDto) {
        return toEntity((DogDto) petDto);
    }

    @Override
    default Pet abstractPetDtoToPet(PetDto petDto) {
        return petDtoToPet(petDto);
    }

    Dog petDtoToPet(PetDto petDto);

}
