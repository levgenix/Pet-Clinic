package com.vet24.web.controllers.pet.reproduction;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Client;
import com.vet24.service.user.ClientService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.reproduction.ReproductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/pet/{petId}/reproduction")
public class ReproductionController {

    private final PetService petService;
    private final ReproductionService reproductionService;
    private final ReproductionMapper reproductionMapper;
    private final ClientService clientService;

    @Autowired
    public ReproductionController(ReproductionService reproductionService, ReproductionMapper reproductionMapper,
                                  PetService petService, ClientService clientService) {
        this.reproductionService = reproductionService;
        this.reproductionMapper = reproductionMapper;
        this.petService = petService;
        this.clientService = clientService;
    }


    @Operation(summary = "get reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok",
                    content = @Content(schema = @Schema(implementation = ReproductionDto.class))),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet or pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
    })
    @GetMapping("/{reproductionId}")
    public ResponseEntity<ReproductionDto> getById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (reproduction == null) {
            throw new NotFoundException("reproduction not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!reproduction.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("reproduction not assigned to this pet");
        }
        ReproductionDto reproductionDto = reproductionMapper.toDto(reproduction);

        return new ResponseEntity<>(reproductionDto, HttpStatus.OK);
    }


    @Operation(summary = "add new reproduction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "reproduction successful created",
                    content = @Content(schema = @Schema(implementation = ReproductionDto.class))),
            @ApiResponse(responseCode = "404", description = "pet with this id not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
    })
    @PostMapping("")
    public ResponseEntity<ReproductionDto> save(@PathVariable Long petId, @Validated(OnCreate.class)
                                                @RequestBody ReproductionDto reproductionDto) {

        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionMapper.toEntity(reproductionDto);
        Client client = clientService.getCurrentClient();

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }

        reproduction.setId(null);
        reproductionService.persist(reproduction);

        pet.addReproduction(reproduction);
        petService.update(pet);

        return new ResponseEntity<>(reproductionMapper.toDto(reproduction), HttpStatus.CREATED);
    }


    @Operation(summary = "update reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful updated",
                    content = @Content(schema = @Schema(implementation = ReproductionDto.class))),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet OR \n" +
                    "reproductionId in path and in body not equals OR \npet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
    })
    @PutMapping("/{reproductionId}")
    public ResponseEntity<ReproductionDto> update(@PathVariable Long petId, @PathVariable Long reproductionId,
                                                   @RequestBody ReproductionDto reproductionDto) {

        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        Client client = clientService.getCurrentClient();

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (reproduction == null) {
            throw new NotFoundException("reproduction not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!reproduction.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("reproduction not assigned to this pet");
        }
        if (!reproductionId.equals(reproductionDto.getId())) {
            throw new BadRequestException("reproductionId in path and in body not equals");
        }
        reproduction = reproductionMapper.toEntity(reproductionDto);
        reproduction.setPet(pet);
        reproductionService.update(reproduction);

        return new ResponseEntity<>(reproductionMapper.toDto(reproduction), HttpStatus.OK);

    }


    @Operation(summary = "delete reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful deleted"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
    })
    @DeleteMapping(value = "/{reproductionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        Client client = clientService.getCurrentClient();

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (reproduction == null) {
            throw new NotFoundException("reproduction not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!reproduction.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("reproduction not assigned to this pet");
        }
        pet.removeReproduction(reproduction);
        petService.update(pet);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
