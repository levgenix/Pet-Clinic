package com.vet24.service.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService{


    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${spring.mail.location}")
    private String mailLocation;
    @Value("${spring.mail.sign}")
    private String mailSign;

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;


    public void sendWelcomeMessage (String emailTo,String name,String tokenLink) throws  MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.addAttachment("template-cover-cat.png",
                new ClassPathResource("template-cover-cat.png"));
        Context context = new Context(Locale.ENGLISH);
        Map<String, Object> model = new HashMap<>();
        model.put("name",name );
        model.put("location", mailLocation);
        model.put("sign" ,mailSign);
        model.put("tokenLink", tokenLink);
        context.setVariables(model);
        String html = templateEngine.process("greeting-letter-template", context);
        helper.setTo(emailTo);
        helper.setText(html, true);
        helper.setSubject("Registration greeting");
        helper.setFrom(mailFrom);
        emailSender.send(message);
    }
}
