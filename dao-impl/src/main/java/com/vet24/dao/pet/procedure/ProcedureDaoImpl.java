package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public class ProcedureDaoImpl extends ReadWriteDaoImpl<Long, Procedure> implements  ProcedureDao {
}
