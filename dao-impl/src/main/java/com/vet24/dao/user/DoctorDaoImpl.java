package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Doctor;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class DoctorDaoImpl extends ReadWriteDaoImpl<Long, Doctor> implements DoctorDao {

    @Override
    public Doctor getDoctorByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT d FROM Doctor d WHERE d.email =:email", Doctor.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
