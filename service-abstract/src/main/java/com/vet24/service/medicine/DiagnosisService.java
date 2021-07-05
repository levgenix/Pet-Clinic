package com.vet24.service.medicine;

import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.ReadWriteService;

public interface DiagnosisService extends ReadWriteService<Long, Diagnosis> {


    void persist(Diagnosis diagnosis);
}
