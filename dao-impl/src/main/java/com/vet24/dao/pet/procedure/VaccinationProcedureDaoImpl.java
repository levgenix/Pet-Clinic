package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.springframework.stereotype.Repository;

@Repository
public class VaccinationProcedureDaoImpl extends ReadWriteDaoImpl<Long, VaccinationProcedure> implements  VaccinationProcedureDao {
}
