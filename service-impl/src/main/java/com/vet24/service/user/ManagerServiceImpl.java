package com.vet24.service.user;

import com.vet24.dao.user.ManagerDao;
import com.vet24.models.user.Manager;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl extends ReadWriteServiceImpl<Long, Manager> implements ManagerService {

    private final ManagerDao managerDao;

    @Autowired
    public ManagerServiceImpl(ManagerDao  managerDao) {
        super(managerDao);
        this.managerDao = managerDao;
    }
}
