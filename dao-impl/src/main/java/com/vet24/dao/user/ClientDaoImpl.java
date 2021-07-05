package com.vet24.dao.user;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class ClientDaoImpl extends ReadWriteDaoImpl<Long, Client> implements ClientDao {

    @Override
    public Client getClientByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT c FROM Client c WHERE c.email =:email", Client.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Client getClientWithPetsByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT c FROM Client c JOIN FETCH c.pets WHERE c.email =:email", Client.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Client getClientWithReactionsByEmail(String email) {
        try {
            return manager
                    .createQuery("SELECT c FROM Client c JOIN FETCH c.commentReactions WHERE c.email =:email", Client.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
