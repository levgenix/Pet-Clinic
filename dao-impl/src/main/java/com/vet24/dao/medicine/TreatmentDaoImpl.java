package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.Treatment;
import org.springframework.stereotype.Repository;

@Repository
public class TreatmentDaoImpl extends ReadWriteDaoImpl<Long, Treatment> implements TreatmentDao {
}
