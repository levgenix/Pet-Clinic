package com.vet24.web.controllers.qrcode;

import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.PetContactMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.models.user.Client;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import com.vet24.util.qrcode.PetContactQrCodeGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/client/pet")
public class PetContactQrCodeController {

    private final PetService petService;
    private final PetContactService petContactService;
    private final PetContactMapper petContactMapper;
    private final ClientService clientService;

    public PetContactQrCodeController(PetService petService, PetContactService petContactService, PetContactMapper petContactMapper, ClientService clientService) {
        this.petService = petService;
        this.petContactService = petContactService;
        this.petContactMapper = petContactMapper;
        this.clientService = clientService;
    }

    @Operation(summary = "Encode and create qr code for pet contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully create the qr code", content = @Content()),
            @ApiResponse(responseCode = "404", description = "PetContact ID is not found"),
            @ApiResponse(responseCode = "404", description = "Pet is not yours")
    })
    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> createPetContactQrCode(@PathVariable("id") Long id) {
        if (!clientService.getCurrentClient().getPets().contains(petService.getByKey(id))){
            throw new NotFoundException("It's pet is not yours");
        }
        if (petContactService.isExistByKey(id)) {
            PetContact petContact = petContactService.getByKey(id);
            String UrlToAlertPetContact = "/api/petFound?petCode=" + petContact.getPetCode();
            String sb = "Имя питомца - " + petContact.getPet().getName() + ", " +
                    "Владелец - " + petContact.getOwnerName() + ", " +
                    "Адрес - " + petContact.getAddress() + ", " +
                    "Телефон - " + petContact.getPhone() + ", " +
                    "Чтобы сообщить владельцу о находке перейдите по адресу - " + UrlToAlertPetContact;
            return ResponseEntity.ok(PetContactQrCodeGenerator.generatePetContactQrCodeImage(sb));
        } else if(petService.isExistByKey(id)) {
            Pet pet = petService.getByKey(id);
            Client client = pet.getClient();
            PetContact petContact = new PetContact();
            petContact.setAddress("");
            petContact.setOwnerName(pet.getClient().getFirstname());
            petContact.setPetCode("");
            petContact.setPhone(8L);
            petContact.setPet(pet);
            //
            //номер находится в карточке petContact, в клиенте нет номера, стоит пока заглушка в виде пустой строки
            //адрес находится в карточке petContact, в клиенте нет адреса, стоит заглушка в виде пустой строки
            //
            petContactService.persist(petContact);

            String urlToAlertPetContact = "/api/petFound?petCode=" + petContact.getPetCode();
            String sb = "Имя питомца - " + pet.getName() + ", " +
                    "Владелец - " + client.getFirstname() + ", " +
                    "Адрес - " + (petContact.getAddress().equals("") ? "владелец еще не успел указать адрес" : petContact.getAddress()) + ", " +
                    "Телефон - " + (petContact.getPhone().toString().length() < 11 ? "владелец не успел указать номер телефона" : petContact.getPhone()) + ", " +
                    "Почта - " + client.getEmail() + ", " +
                    "чтобы сообщить владельцу о находке перейдите по адресу - " + urlToAlertPetContact;
            return new ResponseEntity<>(PetContactQrCodeGenerator.generatePetContactQrCodeImage(sb),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create and update PetContact for qr code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully update the PetContact",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "201", description = "Successfully create the PetContact",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "PetContact is expecting a pet for persist command"),
            @ApiResponse(responseCode = "400", description = "PetContact is expecting a pet for persist command"),
    })
    @PostMapping(value = "/{id}/qr")
    public ResponseEntity<PetContactDto> saveOrUpdatePetContact( @PathVariable("id") Long id,
                                                                 @Valid @RequestBody PetContactDto petContactDto) {

        if (petContactService.isExistByKey(id)) {
            PetContact petContactOld = petContactService.getByKey(id);
            if (petContactDto.getOwnerName() == null || petContactDto.getOwnerName().equals("")) {
                petContactDto.setOwnerName(petContactOld.getOwnerName());
            }
            if (petContactDto.getAddress() == null || petContactDto.getAddress().equals("")) {
                petContactDto.setAddress(petContactOld.getAddress());
            }
            if (petContactDto.getPhone() == null || petContactDto.getPhone() == 0) {
                petContactDto.setPhone(petContactOld.getPhone());
            }
            PetContact petContactNew = petContactMapper.toEntity(petContactDto);
            petContactOld.setOwnerName(petContactNew.getOwnerName());
            petContactOld.setAddress(petContactNew.getAddress());
            petContactOld.setPhone(petContactNew.getPhone());
            petContactService.update(petContactOld);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else if (petService.isExistByKey(id)) {
            if (petContactDto.getOwnerName() == null || petContactDto.getOwnerName().equals("")) {
                throw new BadRequestException("name can't is null or is empty for create Contact");
            }
            if (petContactDto.getAddress() == null || petContactDto.getAddress().equals("")) {
                throw new BadRequestException("Address can't is null or is empty for create Contact");
            }
            if (petContactDto.getPhone() == null || petContactDto.getPhone() == 0) {
                throw new BadRequestException("phone can't is null or empty for create Contact");
            }
            Pet pet = petService.getByKey(id);
            PetContact petContact = petContactMapper.toEntity(petContactDto);
            petContact.setPetCode(petContactService.randomPetContactUniqueCode(id));
            petContact.setPet(pet);
            petContactService.persist(petContact);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}