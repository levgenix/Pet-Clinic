package com.vet24.service.media;

import com.vet24.models.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("application.properties")
public class ResourceServiceImpl implements ResourceService {

    @Value("${application.upload.folder:uploads}")
    private String uploadFolder;

    private String contentTypeDefault = "application/octet-stream";

    private static Map<String, String> contentTypeMap = new HashMap<>();

    static {
        contentTypeMap.put(".mp4", "video/mp4");
        contentTypeMap.put(".mpeg", "video/mpeg");
        contentTypeMap.put(".mpg", "video/mpeg");
        contentTypeMap.put(".ogg", "video/ogg");
        contentTypeMap.put(".3gpp", "video/3gpp");
        contentTypeMap.put(".wmv", "video/x-ms-wmv");
        contentTypeMap.put(".flv", "video/x-flv");
        contentTypeMap.put(".jpeg", "image/jpeg");
        contentTypeMap.put(".jpe", "image/jpeg");
        contentTypeMap.put(".jpg", "image/jpeg");
        contentTypeMap.put(".gif", "image/gif");
        contentTypeMap.put(".png", "image/png");
        contentTypeMap.put(".tiff", "image/tiff");
        contentTypeMap.put(".tif", "image/tiff");
        contentTypeMap.put(".mid", "audio/midi");
        contentTypeMap.put(".midi", "audio/midi");
        contentTypeMap.put(".mp3", "audio/mpeg");
        contentTypeMap.put(".wav", "audio/vnd.wav");
        contentTypeMap.put(".flac", "audio/flac");
        contentTypeMap.put(".m4a", "audio/mp4");
        contentTypeMap.put(".m4b", "audio/mp4");
        contentTypeMap.put(".m3u", "audio/x-mpegurl");
        contentTypeMap.put(".au", "audio/basic");
    }

    public String getContentTypeByFileName(String filename) {
        int extensionIndex = filename.lastIndexOf(".");

        if (extensionIndex < 0) {
            throw new StorageException("Cannot load file [" + filename + "] without extension");
        }

        return contentTypeMap.getOrDefault(
                filename.substring(extensionIndex),
                contentTypeDefault
        );
    }

    @Override
    public byte[] loadAsByteArray(String url) {
        try (InputStream is = new FileSystemResource(url).getInputStream()) {
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            throw new StorageException("File not found: " + url, e);
        }
    }
}
