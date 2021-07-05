package com.vet24.service.medicine;

import com.vet24.dao.medicine.TreatmentDao;
import com.vet24.models.medicine.Treatment;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TreatmentServiceImpl extends ReadWriteServiceImpl<Long, Treatment> implements TreatmentService {

    private final TreatmentDao treatmentDao;

    @Autowired
    protected TreatmentServiceImpl(TreatmentDao treatmentDao) {
        super(treatmentDao);
        this.treatmentDao = treatmentDao;
    }
}
