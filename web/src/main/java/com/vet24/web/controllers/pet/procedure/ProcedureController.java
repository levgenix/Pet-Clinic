package com.vet24.web.controllers.pet.procedure;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.AbstractNewProcedureMapper;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.ProcedureService;
import com.vet24.service.user.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api/client/pet/{petId}/procedure")
@Tag(name = "procedure-controller", description = "operations with Procedures")
public class ProcedureController {

    private final PetService petService;
    private final ProcedureService procedureService;
    private final ProcedureMapper procedureMapper;
    private final AbstractNewProcedureMapper newProcedureMapper;
    private final ClientService clientService;
    private final MedicineService medicineService;

    @Autowired
    public ProcedureController(PetService petService, ProcedureService procedureService,
                               ProcedureMapper procedureMapper, AbstractNewProcedureMapper newProcedureMapper,
                               ClientService clientService, MedicineService medicineService) {
        this.petService = petService;
        this.procedureService = procedureService;
        this.procedureMapper = procedureMapper;
        this.newProcedureMapper = newProcedureMapper;
        this.clientService = clientService;
        this.medicineService = medicineService;
    }

    @Operation(summary = "get a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> getById(@PathVariable Long petId, @PathVariable Long procedureId) {
        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = procedureService.getByKey(procedureId);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (procedure == null) {
            throw new NotFoundException("procedure not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("pet not assigned to this procedure");
        }
        ProcedureDto procedureDto = procedureMapper.toDto(procedure);

        return new ResponseEntity<>(procedureDto, HttpStatus.OK);
    }

    @Operation(summary = "add a new Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("")
    public ResponseEntity<ProcedureDto> save(@PathVariable Long petId, @Validated(OnCreate.class)
                                             @RequestBody AbstractNewProcedureDto newProcedureDto) {

        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = newProcedureMapper.toEntity(newProcedureDto);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }

        Medicine medicine = medicineService.getByKey(newProcedureDto.getMedicineId());
        procedure.setMedicine(medicine);
        procedure.setPet(pet);
        procedureService.persist(procedure);

        pet.addProcedure(procedure);
        petService.update(pet);

        return new ResponseEntity<>(procedureMapper.toDto(procedure), HttpStatus.CREATED);
    }

    @Operation(summary = "update a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> update(@PathVariable Long petId, @PathVariable Long procedureId,
                                         @Validated(OnUpdate.class )@RequestBody ProcedureDto procedureDto) {

        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = procedureService.getByKey(procedureId);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (procedure == null) {
            throw new NotFoundException("procedure not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("pet not assigned to this procedure");
        }
        if (!procedureDto.getId().equals(procedureId)) {
            throw new BadRequestException("procedureId in path and in body not equals");
        }
        procedure = procedureMapper.toEntity(procedureDto);
        Medicine medicine = medicineService.getByKey(procedureDto.getMedicineId());
        procedure.setMedicine(medicine);
        procedure.setPet(pet);
        procedureService.update(procedure);

        return new ResponseEntity<>(procedureMapper.toDto(procedure), HttpStatus.OK);
    }

    @Operation(summary = "delete a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a Procedure"),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{procedureId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long procedureId) {
        Client client = clientService.getCurrentClient();
        Procedure procedure = procedureService.getByKey(procedureId);
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (procedure == null) {
            throw new NotFoundException("procedure not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("pet not assigned to this procedure");
        }

        procedureService.delete(procedure);
        pet.removeProcedure(procedure);
        petService.update(pet);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
