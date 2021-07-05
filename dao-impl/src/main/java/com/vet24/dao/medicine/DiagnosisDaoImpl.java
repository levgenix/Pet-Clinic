package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;

import com.vet24.models.medicine.Diagnosis;
import org.springframework.stereotype.Repository;

@Repository
public class DiagnosisDaoImpl  extends ReadWriteDaoImpl<Long, Diagnosis> implements DiagnosisDao{
}
