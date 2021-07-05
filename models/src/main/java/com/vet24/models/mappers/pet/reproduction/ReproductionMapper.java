package com.vet24.models.mappers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.reproduction.Reproduction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReproductionMapper extends DtoMapper<Reproduction, ReproductionDto>, EntityMapper<ReproductionDto, Reproduction> {

}
