package com.vet24.service.pet.reproduction;

import com.vet24.dao.pet.reproduction.ReproductionDao;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReproductionServiceImpl extends ReadWriteServiceImpl<Long, Reproduction> implements ReproductionService {

    private final ReproductionDao reproductionDao;

    @Autowired
    public ReproductionServiceImpl(ReproductionDao reproductionDao) {
        super(reproductionDao);
        this.reproductionDao = reproductionDao;
    }
}
