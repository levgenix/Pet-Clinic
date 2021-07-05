package com.vet24.dao.user;


import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Client;
import com.vet24.models.user.Doctor;

public interface DoctorDao extends ReadWriteDao<Long, Doctor> {

    Doctor getDoctorByEmail(String email);
}
