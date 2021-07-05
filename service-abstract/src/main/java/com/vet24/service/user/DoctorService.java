package com.vet24.service.user;
import com.vet24.models.user.Doctor;
import com.vet24.service.ReadWriteService;

public interface DoctorService extends ReadWriteService<Long, Doctor> {

    Doctor getCurrentDoctor();

}
