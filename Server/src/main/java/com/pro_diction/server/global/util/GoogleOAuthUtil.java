package com.pro_diction.server.global.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.model.Role;
import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@Slf4j
public class GoogleOAuthUtil {
    @Value(value = "${google.client.id}")
    private String CLIENT_ID;

    public Member authenticate(String idToken) throws GeneralSecurityException, IOException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken == null)
            throw new GeneralException(ErrorCode.INVALID_ID_TOKEN);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        // Get profile information from payload
        String nickname = (String) payload.get("name");
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");

        return Member.builder()
                .google_nickname(nickname)
                .google_email(email)
                .google_profile(pictureUrl)
                .role(Role.USER)
                .build();
    }
}
