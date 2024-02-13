package com.pro_diction.server.global.util;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class WavUtil {
    private final Storage storage;

    @Value("${GCS_BUCKET_NAME}")
    String BUCKET_NAME;

    public String uploadGCSFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = "wav/" + fileName;
        String ext = file.getContentType();

        BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .build();
        storage.createFrom(blobInfo, file.getInputStream());

        return fileName;
    }
}
