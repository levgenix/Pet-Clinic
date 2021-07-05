package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.DoctorReview;

public interface DoctorReviewDao extends ReadWriteDao<Long, DoctorReview> {
    DoctorReview getByDoctorAndClientId(long doctorId, long clientId);
}
