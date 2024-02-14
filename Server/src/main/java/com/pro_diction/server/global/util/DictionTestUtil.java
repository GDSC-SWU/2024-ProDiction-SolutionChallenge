package com.pro_diction.server.global.util;

import com.pro_diction.server.domain.test.dto.TestApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DictionTestUtil {
    @Value("${DICTION_TEST_API_URL}")
    private String DICTION_TEST_API_URL;

    @Value("${DICTION_TEST_API_KEY}")
    private String DICTION_TEST_API_KEY;

    public Double test(MultipartFile file, String pronunciation) throws IOException {
        try {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(3000);
            factory.setReadTimeout(3000);
            RestTemplate restTemplate = new RestTemplate(factory);
            HttpHeaders httpHeaders = new HttpHeaders();
            String audioContents = null;
            Map<String, Object> request = new HashMap<>();
            Map<String, String> argument = new HashMap<>();

            // .wav 음성 파일을 Base64로 인코딩
            byte[] audioBytes = file.getBytes();
            audioContents = Base64.getEncoder().encodeToString(audioBytes);

            // Request Header 구성
            httpHeaders.set("Authorization", DICTION_TEST_API_KEY);

            // Request Body 구성
            argument.put("language_code", "korean");
            argument.put("script", pronunciation);
            argument.put("audio", audioContents);
            request.put("argument", argument);

            // Request Header, Body 요청 구성
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, httpHeaders);

            TestApiResponseDto testApiResponseDto =
                    restTemplate.postForEntity(DICTION_TEST_API_URL, httpEntity, TestApiResponseDto.class).getBody();

            return Math.round(Double.valueOf(testApiResponseDto.getReturn_object().getScore()) / 5 * 100 * 10) / 10.0;
        } catch (Exception e) { // 아예 다른 소리일 경우 timeout 발생
            return 0.0;
        }
    }
}
