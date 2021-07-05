package com.vet24.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.vet24.service.media.MailService;
import com.vet24.util.mailSender.PetFoundMailSender;
import com.vet24.web.config.ClinicDBRider;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(schema = "public", caseInsensitiveStrategy = Orthography.LOWERCASE)
@ClinicDBRider
public abstract class ControllerAbstractIntegrationTest {

    @MockBean
    protected MailService mailService;

    @MockBean
    protected PetFoundMailSender petFoundMailSender;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
