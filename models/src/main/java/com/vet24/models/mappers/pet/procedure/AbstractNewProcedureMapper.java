package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.bridge.MedicineServiceAdapter;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractNewProcedureMapper implements
        DtoMapper<Procedure, AbstractNewProcedureDto> {

    private Map<ProcedureType, AbstractProcedureMapper> mapperMap;

    @Autowired
    private List<AbstractProcedureMapper> mapperList;

    @Autowired
    MedicineServiceAdapter medicineServiceAdapter;

    @PostConstruct
    private void init() {
        this.setMapperMap(mapperList);
    }

    private void setMapperMap(List<AbstractProcedureMapper> mapperList) {
        mapperMap = mapperList.stream().collect(Collectors.toMap(AbstractProcedureMapper::getProcedureType, Function.identity()));
    }

    @Override
    public Procedure toEntity(AbstractNewProcedureDto abstractNewProcedureDto) {
        if (!medicineServiceAdapter.isExistByKey(abstractNewProcedureDto.getMedicineId())) {
            throw new NotFoundException("Medicine with id: " + abstractNewProcedureDto.getMedicineId() + " not found");
        }
        if (mapperMap.containsKey(abstractNewProcedureDto.getType())) {
            return mapperMap.get(abstractNewProcedureDto.getType()).abstractNewProcedureDtoToProcedure(abstractNewProcedureDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find mapper for AbstractNewProcedureDto: " + abstractNewProcedureDto);
        }
    }
}
