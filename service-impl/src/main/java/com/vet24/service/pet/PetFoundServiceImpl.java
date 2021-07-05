package com.vet24.service.pet;

import com.vet24.dao.pet.PetFoundDao;
import com.vet24.models.pet.PetFound;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PetFoundServiceImpl extends ReadWriteServiceImpl<Long, PetFound> implements PetFoundService {

    private final PetFoundDao petFoundDao;

    public PetFoundServiceImpl(PetFoundDao petFoundDao) {
        super(petFoundDao);
        this.petFoundDao = petFoundDao;
    }
}
