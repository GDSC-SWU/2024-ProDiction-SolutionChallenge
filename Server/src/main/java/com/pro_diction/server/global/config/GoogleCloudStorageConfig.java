package com.pro_diction.server.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfig {
    @Value("${GCP_PROJECT_ID}")
    String PROJECT_ID;

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource("apt-cubist-411212-be837d893971.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

        return StorageOptions.newBuilder()
                .setProjectId(PROJECT_ID)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
