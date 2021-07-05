package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.user.Doctor;
import com.vet24.service.user.DoctorService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;

@WithUserDetails(value = "doctor33@gmail.com")
public class DoctorControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    DoctorService doctorService;

    private final String URI = "http://localhost:8090/api/doctor/pet/{petId}/addDiagnosis";

    @Test
    @DataSet(value = {"/datasets/clients.yml","/datasets/doctors.yml"}, cleanBefore = true)
    public void shouldBeNotFound()  {
       String diagnosis = "bla-bla-bla";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(diagnosis, headers);
        ResponseEntity<DiagnosisDto> responseEntity =  testRestTemplate
                .exchange(URI, HttpMethod.POST,entity, DiagnosisDto.class,1001);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DataSet(value = {"/datasets/clients.yml","/datasets/doctors.yml"}, cleanBefore = true)
    public void shouldBeCreated()  {
        String diagnosis = "bla-bla-bla";
        Doctor doctor = doctorService.getCurrentDoctor();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(diagnosis, headers);
        ResponseEntity<DiagnosisDto> responseEntity =  testRestTemplate
                .exchange(URI, HttpMethod.POST,entity, DiagnosisDto.class,101L);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assert.assertEquals("bla-bla-bla",responseEntity.getBody().getDescription());
        Assert.assertEquals(doctor.getId(),responseEntity.getBody().getDoctorId());
        Assert.assertEquals(Long.valueOf(101L),responseEntity.getBody().getPetId());
        Assert.assertNotNull(responseEntity.getBody().getId());
        Assert.assertNotNull(responseEntity.getBody().getDoctorId());
        Assert.assertEquals(responseEntity.getBody().getDoctorId(),doctor.getId());
    }

}
