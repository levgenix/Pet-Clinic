package com.vet24.service.medicine;

import com.vet24.dao.medicine.DiagnosisDao;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisServiceImpl extends ReadWriteServiceImpl<Long,  Diagnosis>
        implements DiagnosisService{
    private final DiagnosisDao diagnosisDao;

    @Autowired
    public DiagnosisServiceImpl(DiagnosisDao  diagnosisDao) {
        super(diagnosisDao);
        this.diagnosisDao = diagnosisDao;

    }


}
