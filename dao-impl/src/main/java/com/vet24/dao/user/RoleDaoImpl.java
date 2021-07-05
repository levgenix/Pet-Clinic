package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends ReadWriteDaoImpl<RoleNameEnum, Role> implements RoleDao {
}
