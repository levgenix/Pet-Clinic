package com.vet24.web.controllers.notification;

import com.vet24.models.dto.googleEvent.GoogleEventDto;
import com.vet24.models.exception.CredentialException;
import com.vet24.models.exception.EventException;
import com.vet24.service.notification.GoogleEventService;

import com.vet24.service.user.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class NotificationController {

    private final GoogleEventService googleEventService;
    private final ClientService clientService;

    public NotificationController(GoogleEventService googleEventService, ClientService clientService) {
        this.googleEventService = googleEventService;
        this.clientService = clientService;
    }


    @Operation(summary = "redirect for google authorization window")
    @GetMapping(value = {"/notification"})
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {
        String redirectURL = googleEventService.getRedirectUrl();
        response.sendRedirect(redirectURL);
    }

    //redirect back to server with accesstoken
    @Operation(summary = "receive and save auth token")
    @GetMapping(value = {"/oauth"})
    public void saveAuthorizationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        if (code != null) {
            googleEventService.saveToken(code, clientService.getCurrentClient().getEmail());
        }
        response.sendRedirect("/");
    }

    @Operation(summary = "create event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully sent"),
            @ApiResponse(responseCode = "400", description = "Dont have credential for this user"),
            @ApiResponse(responseCode = "502", description = "Cannot create event"),
    })
    @PostMapping(value = {"/notification/create"})
    private ResponseEntity<GoogleEventDto> createEvent(@RequestBody GoogleEventDto googleEventDto) throws IOException {
        try {
            googleEventService.createEvent(googleEventDto);
            return new ResponseEntity<>(googleEventDto, HttpStatus.OK);
        } catch (CredentialException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_REQUEST);
        } catch (EventException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_GATEWAY);
        }
    }

    @Operation(summary = "edit event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited"),
            @ApiResponse(responseCode = "502", description = "Cannot edit event"),
    })
    @PostMapping(value = {"/notification/edit"})
    private ResponseEntity<GoogleEventDto> editEvent(@RequestBody GoogleEventDto googleEventDto) throws IOException {
        try {
            googleEventService.editEvent(googleEventDto);
            return new ResponseEntity<>(googleEventDto, HttpStatus.OK);
        } catch (EventException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_GATEWAY);
        }
    }

    @Operation(summary = "delete event on clients google calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "502", description = "Cannot delete event"),
    })
    @PostMapping(value = {"/notification/delete"})
    private ResponseEntity<GoogleEventDto> deleteEvent(@RequestBody GoogleEventDto googleEventDto) throws IOException {
        try {
            googleEventService.deleteEvent(googleEventDto);
            return new ResponseEntity<>(googleEventDto, HttpStatus.OK);
        } catch (EventException e) {
            return new ResponseEntity<>(googleEventDto, HttpStatus.BAD_GATEWAY);
        }
    }
}