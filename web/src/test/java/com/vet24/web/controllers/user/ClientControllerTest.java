package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.config.ClinicDBRider;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@WithUserDetails(value = "client1@email.com")
@DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client";

    private final Principal principal = () -> "client1@email.com";

    @Test
    public void shouldGetResponseEntityClientDto_ForCurrentClient() {
        ResponseEntity<ClientDto> response = testRestTemplate.getForEntity(URI, ClientDto.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(principal.getName(), response.getBody().getEmail());
    }

    @Test
    public void uploadClientAvatarAndVerify() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
        mockMvc.perform(multipart(URI + "/avatar")
                .file(mockMultipartFile).header("Content-Type", "multipart/form-data"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        (result) -> {
                            ResponseEntity<byte[]> response = testRestTemplate.getForEntity(URI + "/avatar", byte[].class);
                            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
                        }
                );
    }
}