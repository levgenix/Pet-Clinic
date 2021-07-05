package com.vet24.service.pet.procedure;

import com.vet24.dao.pet.procedure.VaccinationProcedureDao;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaccinationProcedureServiceImpl extends ReadWriteServiceImpl<Long, VaccinationProcedure> implements VaccinationProcedureService {

    private final VaccinationProcedureDao vaccinationProcedureDao;

    @Autowired
    public VaccinationProcedureServiceImpl(VaccinationProcedureDao vaccinationProcedureDao) {
        super(vaccinationProcedureDao);
        this.vaccinationProcedureDao = vaccinationProcedureDao;
    }
}
