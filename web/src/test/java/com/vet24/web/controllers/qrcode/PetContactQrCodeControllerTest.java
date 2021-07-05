package com.vet24.web.controllers.qrcode;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;

@WithUserDetails(value = "client1@email.com")
public class PetContactQrCodeControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client/pet/{petId}/qr";

    // get create qr code for petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreateQrCodeForViewOnePetContactSuccess() throws Exception {
        ResponseEntity<byte[]> response = testRestTemplate.getForEntity(URI, byte[].class, 107);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreateQrCodeForViewOnePetContactError404Pet() throws Exception {
        ResponseEntity<byte[]> response = testRestTemplate.getForEntity(URI, byte[].class, 1000);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // post update petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testUpdatePetContactForPetSuccess() throws Exception {
        PetContactDto petContact = new PetContactDto("Мария", "Невского 17", 4854789899L);
        HttpEntity<PetContactDto> entity = new HttpEntity<>(petContact, new HttpHeaders());
        ResponseEntity<PetContactDto> response = testRestTemplate.postForEntity(URI, entity, PetContactDto.class, 103);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // post create petContact by id - success
    //@Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreatePetContactForPetSuccess() throws Exception {
        PetContactDto petContact = new PetContactDto("Мария", "Невского 17", 5647564343L);
        HttpEntity<PetContactDto> entity = new HttpEntity<>(petContact, new HttpHeaders());
        ResponseEntity<PetContactDto> response = testRestTemplate.postForEntity(URI, entity, PetContactDto.class, 106);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // post pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testUpdateAndCreatePetContactForPetError404Pet() throws Exception {
        PetContactDto petContact = new PetContactDto("Мария", "Невского 17", 2456786957L);
        HttpEntity<PetContactDto> entity = new HttpEntity<>(petContact, new HttpHeaders());
        ResponseEntity<PetContactDto> response = testRestTemplate.postForEntity(URI, entity, PetContactDto.class, 1000);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
