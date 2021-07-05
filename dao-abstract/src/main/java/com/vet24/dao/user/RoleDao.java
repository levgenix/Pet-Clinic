package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;

public interface RoleDao extends ReadWriteDao<RoleNameEnum, Role> {
}
