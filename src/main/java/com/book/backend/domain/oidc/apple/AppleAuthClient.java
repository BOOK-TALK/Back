package com.book.backend.domain.oidc.apple;

import com.book.backend.domain.oidc.record.OidcPublicKeyList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AppleAuthClient {

    private final RestClient restClient;

    private final String publicKeyUri;

    public AppleAuthClient(
            RestClient restClient,
            @Value("${apple.publicKeyUri}") String publicKeyUri
    ) {
        this.restClient = restClient;
        this.publicKeyUri = publicKeyUri;
    }

    // 공개키 목록 반환
    public OidcPublicKeyList getPublicKeys() {
        return restClient.get()
                .uri(publicKeyUri)
                .retrieve()
                .body(OidcPublicKeyList.class);
    }
}
