package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Manager;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerDaoImpl extends ReadWriteDaoImpl<Long, Manager> implements ManagerDao {
}
