package com.vet24.web.medicine;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.medicine.MedicineDaoImpl;
import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.mappers.medicine.MedicineMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.medicine.MedicineController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "manager@gmail.com")
public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private MedicineMapper medicineMapper;
    @Autowired
    private MedicineController medicineController;
    @Autowired
    private MedicineDaoImpl medicineDao;
    private final String URI = "http://localhost:8090/api/manager/medicine";
    private final HttpHeaders headers = new HttpHeaders();
    private Medicine medicine;
    private MedicineDto medicineDto;

    @Before
    public void createNewMedicineAndMedicineDto() {
        medicine = new Medicine("daulet", "jm", "dsad", "test");
        medicine.setId(1L);
        medicineDto = medicineMapper.toDto(medicine);
    }

    //test controller exist
    @Test
    public void shouldBeMedicineController() throws Exception {
        assertThat(medicineController).isNotNull();
    }

    //get medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeGetMedicineById() throws Exception {
        Medicine medicine = medicineDao.getByKey(100L);
        ResponseEntity<MedicineDto> response = testRestTemplate
                .getForEntity(URI + "/{id}", MedicineDto.class, 100);

        assertThat(medicine).isNotNull();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //add medicine
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeAddMedicine() throws URISyntaxException {
        List<Medicine> medicineListBefore = medicineDao.getAll();
        int countRow = medicineListBefore.size();
        Medicine medicine = new Medicine("tetetete", "etetete", "ttrtrt", "ttrrtr");
        medicine.setId(3L);
        MedicineDto medicineDto = medicineMapper.toDto(medicine);
        HttpEntity<MedicineDto> request = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response = testRestTemplate
                .postForEntity(URI, request, MedicineDto.class);
        List<Medicine> medicineList = medicineDao.getAll();
        int resultRow = medicineList.size();
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assert.assertEquals(++countRow, resultRow);
    }

    //put medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeUpdateMedicineById() throws Exception {
        medicineDto.setId(101L);
        HttpEntity<MedicineDto> entity = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.PUT, entity, MedicineDto.class, 101);
        Medicine updateMedicine = medicineDao.getByKey(101L);
        medicine.setId(101L);
        Assert.assertEquals(medicine, updateMedicine);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //upload icon for medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeUpdateMedicineIcon() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
        mockMvc.perform(multipart(URI + "/{id}/set-pic", 100)
                .file(mockMultipartFile).header("Content-Type", "multipart/form-data"))
                .andExpect(status().isOk());

        ResponseEntity<byte[]> response = testRestTemplate.getForEntity(URI + "/{id}/set-pic", byte[].class, 100);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //delete medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeDeleteMedicine() throws Exception {
        List<Medicine> medicineListBefore = medicineDao.getAll();
        int countRow = medicineListBefore.size();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.DELETE, entity, Void.class, 100);
        List<Medicine> medicineListAfter = medicineDao.getAll();
        int resultRow = medicineListAfter.size();
        Assert.assertEquals(--countRow, resultRow);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //test search medicine
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeSearchMedicine() throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URI + "/search")
                .queryParam("manufactureName")
                .queryParam("name")
                .queryParam("searchText", "java");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
