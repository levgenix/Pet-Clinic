package com.vet24.service.user;

import com.vet24.models.user.Client;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.ReadWriteService;

public interface VerificationService extends ReadWriteService<Long, VerificationToken> {

     String createVerificationToken(Client client);

     VerificationToken getVerificationToken(String token);
}
