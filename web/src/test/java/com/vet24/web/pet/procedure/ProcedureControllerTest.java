package com.vet24.web.pet.procedure;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.procedure.ProcedureDao;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.procedure.ProcedureController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDate;


@WithUserDetails(value = "user3@gmail.com")
public class ProcedureControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    ProcedureController procedureController;

    @Autowired
    ProcedureMapper procedureMapper;

    @Autowired
    ProcedureDao procedureDao;

    final String URI = "http://localhost:8090/api/client/pet";
    final HttpHeaders HEADERS = new HttpHeaders();
    AbstractNewProcedureDto newProcedureDto;
    ProcedureDto procedureDto1;
    ProcedureDto procedureDto3;

    // client 4 --> pet 1 --> procedure 1 (to check client-pet link)
    // client 3 --> pet 2 --> procedure 2 (to check pet-procedure link)
    //        `---> pet 3 --> procedure 3 (to get & update & delete)
    //                  `---> procedure 4 (to create)

    @Before
    public void createModels() {
        this.newProcedureDto = new VaccinationDto(LocalDate.now(), 100L, "4f435", false, null);
        this.procedureDto1 = new ProcedureDto(100L, LocalDate.now(), ProcedureType.EXTERNAL_PARASITE,
                100L, "4f435", true, 20);
        this.procedureDto3 = new ProcedureDto(102L, LocalDate.now(), ProcedureType.EXTERNAL_PARASITE,
                100L, "4f435", true, 20);
    }

    // GET procedure by id - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testGetProcedureSuccess() {
        ProcedureDto procedureDtoFromDao = procedureMapper
                .toDto(procedureDao.getByKey(102L));
        ResponseEntity<ProcedureDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/procedure/{procedureId}", ProcedureDto.class, 102, 102);

        Assert.assertEquals(response.getBody(), procedureDtoFromDao);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    // GET procedure by id - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testGetProcedureErrorPetNotFound() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/procedure/{procedureId}", ExceptionDto.class, 33, 102);

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // GET procedure by id - 404 ERROR "procedure not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "/datasets/reproduction.yml"})
    public void testGetProcedureErrorProcedureNotFound() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/procedure/{procedureId}", ExceptionDto.class, 102, 33);

        Assert.assertEquals(response.getBody(), new ExceptionDto("procedure not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // GET procedure by id - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testGetProcedureErrorPetForbidden() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/procedure/{procedureId}", ExceptionDto.class, 100, 100);

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // GET procedure by id - 400 ERROR "pet not assigned to this procedure"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testGetProcedureErrorProcedureForbidden() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/procedure/{procedureId}", ExceptionDto.class, 101, 102);

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not assigned to this procedure"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // ADD new procedure - 201 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testAddProcedureSuccess() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<AbstractNewProcedureDto> request = new HttpEntity<>(newProcedureDto, HEADERS);
        ResponseEntity<ProcedureDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/procedure", request, ProcedureDto.class, 102);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    // ADD new procedure - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testAddProcedureErrorPetNotFound() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<AbstractNewProcedureDto> request = new HttpEntity<>(newProcedureDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/procedure", request, ExceptionDto.class, 33);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // ADD new procedure - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testAddProcedureErrorPetForbidden() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<AbstractNewProcedureDto> request = new HttpEntity<>(newProcedureDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/procedure", request, ExceptionDto.class, 100);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // UPDATE  procedure - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testUpdateProcedureSuccess() {
        int beforeCount = procedureDao.getAll().size();
        ProcedureDto procedureDtoBefore = procedureMapper.toDto(procedureDao.getByKey(102L));
        HttpEntity<ProcedureDto> request = new HttpEntity<>(procedureDto3, HEADERS);
        ResponseEntity<ProcedureDto> response = testRestTemplate
                .exchange(URI + "/{petId}/procedure/{procedureId}", HttpMethod.PUT, request, ProcedureDto.class, 102, 102);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), procedureDto3);
        Assert.assertNotEquals(response.getBody(), procedureDtoBefore);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    // UPDATE  procedure - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testUpdateProcedureErrorPetNotFound() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<ProcedureDto> request = new HttpEntity<>(procedureDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/procedure/{procedureId}", HttpMethod.PUT, request, ExceptionDto.class, 33, 102);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // UPDATE  procedure - 404 ERROR "procedure not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testUpdateProcedureErrorProcedureNotFound() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<ProcedureDto> request = new HttpEntity<>(procedureDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/procedure/{procedureId}", HttpMethod.PUT, request, ExceptionDto.class, 102, 33);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("procedure not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // UPDATE  procedure - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testUpdateProcedureErrorPetForbidden() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<ProcedureDto> request = new HttpEntity<>(procedureDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/procedure/{procedureId}", HttpMethod.PUT, request, ExceptionDto.class, 100, 100);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // UPDATE  procedure - 400 ERROR "pet not assigned to this procedure"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testUpdateProcedureErrorProcedureForbidden() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<ProcedureDto> request = new HttpEntity<>(procedureDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/procedure/{procedureId}", HttpMethod.PUT, request, ExceptionDto.class, 101, 102);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not assigned to this procedure"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // UPDATE  procedure - 400 ERROR "procedureId in path and in body not equals"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testUpdateProcedureErrorIdDosentMatch() {
        int beforeCount = procedureDao.getAll().size();
        HttpEntity<ProcedureDto> request = new HttpEntity<>(procedureDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/procedure/{procedureId}", HttpMethod.PUT, request, ExceptionDto.class, 102, 102);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("procedureId in path and in body not equals"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // DELETE procedure - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testDeleteProcedureSuccess() {
        int beforeCount = procedureDao.getAll().size();
        ResponseEntity<Void> response = testRestTemplate.exchange(URI + "/{petId}/procedure/{procedureId}",
                HttpMethod.DELETE, new HttpEntity<>(HEADERS), Void.class, 102, 102);
        int afterCount = procedureDao.getAll().size();
        Procedure procedure = procedureDao.getByKey(102L);

        Assert.assertNull(procedure);
        Assert.assertEquals(--beforeCount, afterCount);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    // DELETE procedure - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testDeleteProcedureErrorPetNotFound() {
        int beforeCount = procedureDao.getAll().size();
        ResponseEntity<ExceptionDto> response = testRestTemplate.exchange(URI + "/{petId}/procedure/{procedureId}",
                HttpMethod.DELETE, new HttpEntity<>(HEADERS), ExceptionDto.class, 33, 102);
        int afterCount = procedureDao.getAll().size();
        Procedure procedure = procedureDao.getByKey(102L);

        Assert.assertNotNull(procedure);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // DELETE procedure - 404 ERROR "procedure not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testDeleteProcedureErrorProcedureNotFound() {
        int beforeCount = procedureDao.getAll().size();
        ResponseEntity<ExceptionDto> response = testRestTemplate.exchange(URI + "/{petId}/procedure/{procedureId}",
                HttpMethod.DELETE, new HttpEntity<>(HEADERS), ExceptionDto.class, 102, 33);
        int afterCount = procedureDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("procedure not found"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // DELETE procedure - 400 ERROR "pet not yours"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testDeleteProcedureErrorPetForbidden() {
        int beforeCount = procedureDao.getAll().size();
        ResponseEntity<ExceptionDto> response = testRestTemplate.exchange(URI + "/{petId}/procedure/{procedureId}",
                HttpMethod.DELETE, new HttpEntity<>(HEADERS), ExceptionDto.class, 100, 100);
        int afterCount = procedureDao.getAll().size();
        Procedure procedure = procedureDao.getByKey(100L);

        Assert.assertNotNull(procedure);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // DELETE procedure - 400 ERROR "pet not assigned to this procedure"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
    public void testDeleteProcedureErrorProcedureForbidden() {
        int beforeCount = procedureDao.getAll().size();
        ResponseEntity<ExceptionDto> response = testRestTemplate.exchange(URI + "/{petId}/procedure/{procedureId}",
                HttpMethod.DELETE, new HttpEntity<>(HEADERS), ExceptionDto.class, 101, 102);
        int afterCount = procedureDao.getAll().size();
        Procedure procedure = procedureDao.getByKey(102L);

        Assert.assertNotNull(procedure);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not assigned to this procedure"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
