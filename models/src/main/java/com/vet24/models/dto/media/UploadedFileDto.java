package com.vet24.models.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadedFileDto {
    private String filename;
    private String url;
}
