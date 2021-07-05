package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@Slf4j
@DBRider
public class RegistrationControllerTest extends ControllerAbstractIntegrationTest {

    final String URI = "http://localhost:8090/api/registration";

    @Test
    @DataSet(value = "/datasets/user-entities.yml", cleanBefore = true)
    public void shouldBeNotAcceptableWrongEmail() throws Exception {
        RegisterDto registerDto = new RegisterDto("342354234.com","Vera","P",
                "Congo","Congo");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.valueToTree(registerDto).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Valid email is required")));
        Mockito.verify(mailService, times(0)).sendWelcomeMessage(anyString(), anyString(), anyString());
    }

    @Test
    @DataSet(value = "/datasets/user-entities.yml", cleanBefore = true)
    public void shouldBeNotAcceptablePasswords() throws Exception {
        RegisterDto registerDto = new RegisterDto("342354234@gmail.com","Vera","P",
                "Congo","Congo2");
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.valueToTree(registerDto).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Passwords don't match")));
        Mockito.verify(mailService, times(0)).sendWelcomeMessage(anyString(), anyString(), anyString());
    }

    @Test
    @DataSet(value = "/datasets/user-entities.yml", cleanBefore = true)
    public void shouldBeCreated() throws Exception {
        RegisterDto registerDto = new RegisterDto("342354234@gmail.com","Vera","P",
                "Congo","Congo");
        Mockito.doNothing()
                .when(mailService)
                .sendWelcomeMessage(anyString(), anyString(), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.valueToTree(registerDto).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Mockito.verify(mailService)
                .sendWelcomeMessage(eq("342354234@gmail.com"), eq("Vera"), anyString());
    }
}