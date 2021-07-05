package com.vet24.web.controllers.user;

import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.models.user.Client;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/client")
@Tag(name = "client-controller", description = "operations with current Client")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final UploadService uploadService;
    private final ResourceService resourceService;
    private final CommentService commentService;

    public ClientController(ClientService clientService, ClientMapper clientMapper, UploadService uploadService, ResourceService resourceService, CommentService commentService) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
        this.uploadService = uploadService;
        this.resourceService = resourceService;
        this.commentService = commentService;

    }

    @GetMapping("")
    @Operation(summary = "get current Client with his pets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Client",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "404", description = "Client not found exception",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "406", description = "Client not acceptable type",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<ClientDto> getCurrentClient() {
        ClientDto clientDto = clientMapper.toDto(clientService.getCurrentClient());
        return clientDto != null ? ResponseEntity.ok(clientDto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "get avatar of a Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the avatar"),
            @ApiResponse(responseCode = "404", description = "Client or avatar is not found")
    })
    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getClientAvatar() {
        Client client = clientService.getCurrentClient();
        if (client != null) {
            String url = client.getAvatar();
            if (url != null) {
                return new ResponseEntity<>(resourceService.loadAsByteArray(url), addContentHeaders(url), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "upload avatar for a Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded the avatar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadedFileDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)
    })
    @PostMapping(value = "/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadedFileDto> persistClientAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        Client client = clientService.getCurrentClient();
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UploadedFileDto uploadedFileDto = uploadService.store(file);
        client.setAvatar(uploadedFileDto.getUrl());
        clientService.update(client);

        return new ResponseEntity<>(uploadedFileDto, HttpStatus.OK);
    }

    private HttpHeaders addContentHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", resourceService.getContentTypeByFileName(filename));
        return headers;
    }
}
