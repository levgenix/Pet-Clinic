package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository
public class VerificationDaoImpl extends ReadWriteDaoImpl<Long, VerificationToken>  implements VerificationDao{
}
