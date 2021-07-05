package com.vet24.service.user;

import com.vet24.dao.user.RoleDao;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl<RoleNameEnum, Role> implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
        this.roleDao = roleDao;
    }
}
