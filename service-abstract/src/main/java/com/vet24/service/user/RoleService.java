package com.vet24.service.user;

import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Role;
import com.vet24.service.ReadWriteService;

public interface RoleService extends ReadWriteService<RoleNameEnum, Role> {
}
