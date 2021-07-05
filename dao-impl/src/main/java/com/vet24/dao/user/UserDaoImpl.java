package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<Long, User> implements UserDao {
    @Override
    public Optional<User> getByEmail(String email) {
        return manager
                .createQuery("FROM User where email = :email", User.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public User getByUserEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT u FROM User u WHERE u.email =:email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
