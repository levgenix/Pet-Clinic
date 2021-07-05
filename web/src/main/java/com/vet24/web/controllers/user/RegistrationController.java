package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.exception.RepeatedRegistrationException;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.media.MailService;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.VerificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Tag(name = "registration-controller", description = "operations with creation of new clients")
public class RegistrationController {


    private static  final String CONFIRMATION_API ="/api/auth/submit";
    private static final String INVALID_TOKEN_MSG = "Registration token is invalid";
    private static final String PASSWORDS_UNMATCHED = "Passwords don't match";
    @Value("${registration.repeated.error.msg}")
    private  String repeatedRegistrationMsg;
    @Value("${application.domain.name}")
    private  String domainPath;

    private final ClientService clientService;
    private final MailService mailService;
    private final ClientMapper clientMapper;
    private final VerificationService verificationService;


    public RegistrationController(ClientService clientService, MailService mailService,
                                  ClientMapper clientMapper, VerificationService verificationService) {
        this.clientService = clientService;
        this.mailService = mailService;
        this.clientMapper = clientMapper;
        this.verificationService = verificationService;
    }

    @Operation(summary = "Register new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new client"),
            @ApiResponse(responseCode = "417", description = "MessagingException"),
            @ApiResponse(responseCode = "406", description = "Inconsistent input data"),
            @ApiResponse(responseCode = "400", description = "DataIntegrityViolationException")
    })

    @PostMapping("/api/registration")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto
                                                     inputDto,
                                         HttpServletRequest request)throws IOException, MessagingException {
        if(!inputDto.getPassword().equals(inputDto.getConfirmPassword())){
            throw new BadRequestException(PASSWORDS_UNMATCHED);
        }

        Client foundOrNew = clientService.getClientByEmail(inputDto.getEmail());
        if(foundOrNew == null){
            foundOrNew = clientMapper.toEntity(inputDto);
        }
        else if(foundOrNew.getRole().getName()!=RoleNameEnum.UNVERIFIED_CLIENT) {
            throw new RepeatedRegistrationException(repeatedRegistrationMsg);
        }

        foundOrNew.setRole(new Role(RoleNameEnum.UNVERIFIED_CLIENT));

        String tokenLink = domainPath +
                request.getContextPath() +
                RegistrationController.CONFIRMATION_API +
                "?userCode=" +
                verificationService.createVerificationToken(foundOrNew);

        mailService.sendWelcomeMessage(inputDto.getEmail(),inputDto.getFirstname(), tokenLink);

        return new  ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Confirm email of a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Successfully verified new client"),
            @ApiResponse(responseCode = "400", description = "BadRequestException")
    })

    @GetMapping(CONFIRMATION_API)
    public ResponseEntity<ClientDto> verifyMail(@RequestParam String userCode) {

        VerificationToken token = verificationService.getVerificationToken(userCode);
        if (token == null) {
            throw new BadRequestException(INVALID_TOKEN_MSG);
        }
        Client cl = token.getClient();
        cl.setRole(new Role(RoleNameEnum.CLIENT));
        ClientDto clientUpdated = clientMapper.toDto(clientService.update(cl));
        verificationService.delete(token);
        return  ResponseEntity.status(HttpStatus.RESET_CONTENT).body(clientUpdated);

    }

}

