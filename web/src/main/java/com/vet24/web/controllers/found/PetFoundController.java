package com.vet24.web.controllers.found;

import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.mappers.pet.PetFoundMapper;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.PetFound;
import com.vet24.service.pet.PetContactService;
import com.vet24.service.pet.PetFoundService;
import com.vet24.util.mailSender.PetFoundMailSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PetFoundController {

    private final PetFoundService petFoundService;
    private final PetContactService petContactService;
    private final PetFoundMapper petFoundMapper;
    private final PetFoundMailSender petFoundMailSender;

    public PetFoundController(PetFoundService petFoundService, PetContactService petContactService,
                              PetFoundMapper petFoundMapper, PetFoundMailSender petFoundMailSender) {
        this.petFoundService = petFoundService;
        this.petContactService = petContactService;
        this.petFoundMapper = petFoundMapper;
        this.petFoundMailSender = petFoundMailSender;
    }

    /* Запрос может выглядеть следующим образом.
    {
        "latitude" : "1.2345678",
        "longitude" : "2.3456789",
        "text" : "Какой-то текст"
    }*/
    @Operation(summary = "Save data found pet and create with send owner message about pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully save data found and create message",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "PetContact by petCode is not found"),
    })
    @PostMapping(value = "/petFound")
    public ResponseEntity<PetFoundDto> savePetFoundAndSendOwnerPetMessage(@RequestParam(value = "petCode", required = false) String petCode,
                                                                          @RequestBody PetFoundDto petFoundDto) {
        if (petContactService.isExistByPetCode(petCode)) {
            PetContact petContact = petContactService.getByPetCode(petCode);
            PetFound petFound = petFoundMapper.toEntity(petFoundDto);
            petFound.setPet(petContact.getPet());
            petFoundService.persist(petFound);

            String text = petFound.getText();
            String latitude = petFound.getLatitude();
            String longitude = petFound.getLongitude();
            String clientEmail = petContact.getPet().getClient().getEmail();
            String clientName = petContact.getPet().getClient().getFirstname();
            String petName = petContact.getPet().getName();
            String message = String.format("%s, добрый день! Ваш %s нашёлся!\n\n Кто-то отсканировал QR код" +
                    " на ошейнике вашего питомца и отправил вам следующее сообщение: \n\"%s\"\n\n" +
                    "Перейдите по ссылке для просмотра местонахождения питомца: https://www.google.com/maps/place/%s+%s",
                    clientName, petName, text, latitude, longitude);
            petFoundMailSender.sendTextAndGeolocationPet(clientEmail, "Информация о вашем питомце", message);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
