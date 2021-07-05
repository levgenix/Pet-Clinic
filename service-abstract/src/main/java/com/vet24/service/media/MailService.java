package com.vet24.service.media;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MailService {

    void sendWelcomeMessage (String email, String name,String tokenURL)
            throws IOException, MessagingException;
}
