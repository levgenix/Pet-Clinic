package com.vet24.dao.pet.reproduction;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.reproduction.Reproduction;
import org.springframework.stereotype.Repository;

@Repository
public class ReproductionDaoImpl extends ReadWriteDaoImpl<Long, Reproduction> implements ReproductionDao {
}
