package com.vet24.service.media;

import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@PropertySource("application.properties")
public class UploadServiceImpl implements UploadService {

    @Value("${application.upload.folder:uploads}")
    private String uploadFolder;

    @Override
    public UploadedFileDto store(MultipartFile file) throws IOException {
        String originFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        LocalDateTime now = LocalDateTime.now();
        int extensionIndex = originFilename.lastIndexOf(".");

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + originFilename);
        }
        if (extensionIndex < 0) {
            throw new StorageException("Cannot store file [" + originFilename + "] without extension");
        }

        String storageFolder = uploadFolder
                + File.separator
                + now.format(DateTimeFormatter.ofPattern(
                "yyyy" + File.separator + "MM" + File.separator + "dd"))
                + File.separator;

        String storageFilename = originFilename.substring(0, extensionIndex)
                + now.format(DateTimeFormatter.ofPattern("_yyyy-MM-dd_HH-mm-ss"))
                + originFilename.substring(extensionIndex);

        InputStream inputStream = file.getInputStream();
        if (!Files.exists(Paths.get(storageFolder))) {
            Files.createDirectories(Paths.get(storageFolder));
        }
        Files.copy(inputStream, Paths.get(storageFolder + storageFilename),StandardCopyOption.REPLACE_EXISTING);
        inputStream.close();

        return new UploadedFileDto(storageFilename, storageFolder + storageFilename);
    }
}
