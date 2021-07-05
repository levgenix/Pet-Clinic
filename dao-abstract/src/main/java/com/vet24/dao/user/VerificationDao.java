package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.User;
import com.vet24.models.user.VerificationToken;

public interface VerificationDao extends ReadWriteDao<Long, VerificationToken> {
}
