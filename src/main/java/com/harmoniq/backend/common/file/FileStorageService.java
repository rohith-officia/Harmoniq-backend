package com.harmoniq.backend.common.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeAudioFile(MultipartFile file);
}