package com.vet24.service.user;

import com.vet24.dao.user.ClientDao;
import com.vet24.models.user.Client;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl extends ReadWriteServiceImpl<Long, Client> implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl( ClientDao clientDao) {
        super(clientDao);
        this.clientDao = clientDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        return clientDao.getClientByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Client getCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return clientDao.getClientByEmail(authentication.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Client getCurrentClientWithPets() {
        return clientDao.getClientWithPetsByEmail(getCurrentClient().getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public Client getCurrentClientWithReactions() {
        return clientDao.getClientWithReactionsByEmail(getCurrentClient().getUsername());
    }
}
