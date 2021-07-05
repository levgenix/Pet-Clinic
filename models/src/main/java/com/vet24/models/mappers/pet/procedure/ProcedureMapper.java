package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class ProcedureMapper implements
        DtoMapper<Procedure, ProcedureDto>, EntityMapper<ProcedureDto, Procedure> {


    private Map<ProcedureType, AbstractProcedureMapper> mapperMap;

    @Autowired
    private List<AbstractProcedureMapper> mapperList;


    @PostConstruct
    private void init() {
        this.setMapperMap(mapperList);
    }

    private void setMapperMap(List<AbstractProcedureMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractProcedureMapper::getProcedureType, Function.identity()));
    }

    @Mapping(source = "medicine.id", target = "medicineId")
    @Override
    public abstract ProcedureDto toDto (Procedure procedure);

    @Override
    public Procedure toEntity (ProcedureDto procedureDto) {
        if (mapperMap.containsKey(procedureDto.getType())) {
            return mapperMap.get(procedureDto.getType()).procedureDtoToProcedure(procedureDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find mapper for ProcedureDto: " + procedureDto);
        }
    }
}
