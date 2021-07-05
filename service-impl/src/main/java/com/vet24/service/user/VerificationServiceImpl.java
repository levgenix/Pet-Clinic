package com.vet24.service.user;

import com.eatthepath.uuid.FastUUID;
import com.vet24.dao.user.VerificationDao;
import com.vet24.models.user.Client;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VerificationServiceImpl extends ReadWriteServiceImpl<Long, VerificationToken>
        implements VerificationService{
    private final VerificationDao verificationDao;

    @Autowired
    protected VerificationServiceImpl(VerificationDao  verificationDao) {
        super(verificationDao);
        this.verificationDao = verificationDao;

    }

    @Override
    @Transactional
    public  String createVerificationToken(Client client){
        UUID randomUUID =  UUID.randomUUID();
        Long tokenId = randomUUID.getLeastSignificantBits()*37+11;
        persistTokenWithClient(client,tokenId);
        return FastUUID.toString(randomUUID);
    }


    private  void persistTokenWithClient(Client client,Long tokenId){
        VerificationToken vt = new VerificationToken(tokenId,client);
        verificationDao.persist(vt);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationToken getVerificationToken(String token) {
        Long tokenId = FastUUID.parseUUID(token).getLeastSignificantBits()*37+11;
        return verificationDao.getByKey(tokenId);
    }

}
