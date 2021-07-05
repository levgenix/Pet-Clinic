package com.vet24.service.pet;

import com.vet24.dao.pet.PetDao;
import com.vet24.models.pet.Pet;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl extends ReadWriteServiceImpl<Long, Pet> implements PetService {

    private final PetDao petDao;

    public PetServiceImpl(PetDao petDao) {
        super(petDao);
        this.petDao = petDao;
    }
}
