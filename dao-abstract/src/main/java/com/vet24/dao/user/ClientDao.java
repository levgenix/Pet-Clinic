package com.vet24.dao.user;


import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Client;


public interface ClientDao extends ReadWriteDao<Long, Client> {

    Client getClientByEmail(String email);

    Client getClientWithPetsByEmail(String email);

    Client getClientWithReactionsByEmail(String email);
}
