package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.user.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PetMapper.class})
public interface ClientMapper extends DtoMapper<Client, RegisterDto>, EntityMapper<ClientDto, Client> {

}
