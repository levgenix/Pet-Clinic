package com.vet24.models.mappers.medicine;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.medicine.Medicine;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineMapper extends DtoMapper<Medicine, MedicineDto>, EntityMapper<MedicineDto, Medicine> {

}
