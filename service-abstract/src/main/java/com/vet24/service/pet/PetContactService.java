package com.vet24.service.pet;

import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface PetContactService extends ReadWriteService<Long, PetContact> {

    List<String> getAllPetCode();
    boolean isExistByPetCode(String petCode);
    PetContact getByPetCode(String petCode);
    int getCountId();
    String randomPetContactUniqueCode(Long id);
}
