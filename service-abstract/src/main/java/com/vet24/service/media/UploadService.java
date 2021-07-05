package com.vet24.service.media;

import com.vet24.models.dto.media.UploadedFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    UploadedFileDto store(MultipartFile file) throws IOException;
}
