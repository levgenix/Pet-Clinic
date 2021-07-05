package com.vet24.web.controllers.medicine;
import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.medicine.MedicineMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.medicine.MedicineService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("api/manager/medicine")
public class MedicineController {

    private final MedicineService medicineService;
    private final MedicineMapper medicineMapper;
    private final ResourceService resourceService;
    private final UploadService uploadService;

    public MedicineController(MedicineService medicineService, MedicineMapper medicineMapper,
                              ResourceService resourceService, UploadService uploadService) {
        this.medicineService = medicineService;
        this.medicineMapper = medicineMapper;
        this.resourceService = resourceService;
        this.uploadService = uploadService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getById(@PathVariable("id") Long id) {
        Medicine medicine = medicineService.getByKey(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            MedicineDto medicineDto = medicineMapper.toDto(medicine);
            return new ResponseEntity<>(medicineDto, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable(name = "id") Long id) {
        Medicine medicine = medicineService.getByKey(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            medicineService.delete(medicine);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDto> update(@PathVariable Long id,@Valid
                                               @RequestBody MedicineDto medicineDto) {

        Medicine medicine = medicineService.getByKey(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            medicine = medicineMapper.toEntity(medicineDto);
            medicine.setId(id);
            medicineService.update(medicine);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<MedicineDto> save(@Valid @RequestBody MedicineDto medicineDto) {

        Medicine medicine = medicineMapper.toEntity(medicineDto);
        medicine.setId(null);
        try {
            medicineService.persist(medicine);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/set-pic")
    public ResponseEntity<byte[]> getPic(@PathVariable("id") Long id) {
        Medicine medicine = medicineService.getByKey(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String url = medicine.getIcon();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", resourceService.getContentTypeByFileName(url));
            return url != null
                    ? new ResponseEntity<>(resourceService.loadAsByteArray(url), headers, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{id}/set-pic", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadedFileDto> savePic(@PathVariable Long id
            , @RequestParam("file") MultipartFile file) throws IOException {
        Medicine medicine = medicineService.getByKey(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            UploadedFileDto uploadedFileDto = uploadService.store(file);
            medicine.setIcon(uploadedFileDto.getUrl());
            medicineService.update(medicine);
            return new ResponseEntity<>(uploadedFileDto, HttpStatus.OK);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineDto>> search(
            @RequestParam(required = false, name = "manufactureName", defaultValue = "") String manufactureName,
            @RequestParam(required = false, name = "name", defaultValue = "") String name,
            @RequestParam(required = false, name = "searchText", defaultValue = "") String searchText) {
        List<Medicine> medicineList = medicineService.searchFull(manufactureName, name, searchText);
        List<MedicineDto> medicineDtoList = medicineMapper.toDto(medicineList);
        return new ResponseEntity<>(medicineDtoList, HttpStatus.OK);
    }
}



