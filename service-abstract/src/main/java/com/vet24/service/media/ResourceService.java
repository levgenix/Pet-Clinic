package com.vet24.service.media;

public interface ResourceService {

    String getContentTypeByFileName(String filename);

    byte[] loadAsByteArray(String filename);
}
