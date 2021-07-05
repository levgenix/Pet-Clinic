package com.vet24.web.pet.reproduction;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.reproduction.ReproductionDao;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.reproduction.ReproductionController;
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
public class ReproductionControllerTest extends ControllerAbstractIntegrationTest {
    @Autowired
    ReproductionController reproductionController;
    @Autowired
    ReproductionMapper reproductionMapper;
    @Autowired
    ReproductionDao reproductionDao;
    @Autowired
    PetDao petDao;

    final String URI = "http://localhost:8090/api/client/pet";
    final HttpHeaders HEADERS = new HttpHeaders();

    ReproductionDto reproductionDtoNew;
    ReproductionDto reproductionDto1;
    ReproductionDto reproductionDto3;

    // client 4 --> pet 1 --> reproduction 1 (to check client-pet link)
    // client 3 --> pet 2 --> reproduction 2 (to check pet-reproduction link)
    //        `---> pet 3 --> reproduction 3 (to get & update & delete)
    //                  `---> reproduction 4 (to create)

    @Before
    public void createNewReproductionAndReproductionDto() {
        this.reproductionDtoNew = new ReproductionDto(4L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 4);
        this.reproductionDto1 = new ReproductionDto(100L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 11);
        this.reproductionDto3 = new ReproductionDto(102L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 33);
    }

    // get reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionSuccess() {
        ReproductionDto dtoFromDao = reproductionMapper.toDto(reproductionDao.getByKey(102L));
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ReproductionDto.class, 102, 102);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get reproduction by id -  error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError404pet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 33, 102);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError404reproduction() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 102, 33);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError400refPetReproduction() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 101, 102);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // get reproduction by id -  error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testGetReproductionError400refClientPet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 100, 100);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // add reproduction - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDtoNew, HEADERS);
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ReproductionDto.class, 102);
        int afterCount = reproductionDao.getAll().size();

        reproductionDtoNew.setId(response.getBody().getId());
        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), reproductionDtoNew);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // add reproduction - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ExceptionDto.class, 33);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // add reproduction - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testAddReproductionError400() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDtoNew, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ExceptionDto.class, 100);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ReproductionDto.class, 102, 102);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(reproductionDto3, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // put reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError404reproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 33, 102);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError404pet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 102, 33);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400refPetReproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 101, 102);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproductionId in path and in body not equals
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400idInPathAndBody() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 102, 102);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproductionId in path and in body not equals"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testPutReproductionError400refClientPet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 100, 100);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 102, 102);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(102L);

        Assert.assertNull(afterReproduction);
        Assert.assertEquals(--beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // delete reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError404reproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 33, 102);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(101L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError404pet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 102, 33);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError400refPetReproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 101, 102);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(102L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - error pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/reproduction.yml"})
    public void testDeleteReproductionError400refClientPet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 100, 100);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(100L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
