package com.vet24.service.pet.procedure;

import com.vet24.dao.pet.procedure.EchinococcusProcedureDao;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EchinococcusProcedureServiceImpl extends ReadWriteServiceImpl<Long, EchinococcusProcedure> implements EchinococcusProcedureService {

    private final EchinococcusProcedureDao echinococcusProcedureDao;

    @Autowired
    public EchinococcusProcedureServiceImpl(EchinococcusProcedureDao echinococcusProcedureDao) {
        super(echinococcusProcedureDao);
        this.echinococcusProcedureDao = echinococcusProcedureDao;
    }
}
