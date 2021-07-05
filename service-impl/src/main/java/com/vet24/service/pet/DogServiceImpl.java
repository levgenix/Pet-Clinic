package com.vet24.service.pet;

import com.vet24.dao.pet.DogDao;
import com.vet24.models.pet.Dog;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DogServiceImpl extends ReadWriteServiceImpl<Long, Dog> implements DogService {

    private final DogDao dogDao;

    public DogServiceImpl( DogDao dogDao) {
        super(dogDao);
        this.dogDao = dogDao;
    }
}
