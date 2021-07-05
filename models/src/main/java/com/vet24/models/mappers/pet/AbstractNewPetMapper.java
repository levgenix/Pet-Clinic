package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.pet.Pet;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractNewPetMapper implements DtoMapper<Pet, AbstractNewPetDto> {

    private Map<PetType, AbstractPetMapper> mapperMap;

    @Autowired
    private List<AbstractPetMapper> mapperList;

    @PostConstruct
    private void init() {
        this.setMapperMap(mapperList);
    }

    private void setMapperMap(List<AbstractPetMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractPetMapper::getPetType, Function.identity()));
    }

    public Pet toEntity(AbstractNewPetDto abstractNewPetDto) {
        if (mapperMap.containsKey(abstractNewPetDto.getPetType())) {
            return mapperMap.get(abstractNewPetDto.getPetType()).abstractNewPetDtoToPet(abstractNewPetDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + abstractNewPetDto);
        }
    }
}
