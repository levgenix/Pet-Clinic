package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import org.springframework.stereotype.Repository;

@Repository
public class EchinococcusProcedureDaoImpl extends ReadWriteDaoImpl<Long, EchinococcusProcedure> implements  EchinococcusProcedureDao {
}
