package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import org.springframework.stereotype.Repository;

@Repository
public class ExternalParasiteProcedureDaoImpl extends ReadWriteDaoImpl<Long, ExternalParasiteProcedure> implements  ExternalParasiteProcedureDao {
}
