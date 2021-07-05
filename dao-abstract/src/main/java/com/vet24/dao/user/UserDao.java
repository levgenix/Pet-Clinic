package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.User;

import java.util.Optional;

public interface UserDao extends ReadWriteDao<Long, User> {

    Optional<User> getByEmail(String email);

    User getByUserEmail(String email);
}
