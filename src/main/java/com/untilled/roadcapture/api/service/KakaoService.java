package com.untilled.roadcapture.api.service;

import com.google.gson.Gson;
import com.untilled.roadcapture.config.security.dto.KakaoProfile;
import com.untilled.roadcapture.config.security.dto.OAuthTokenResponse;
import com.untilled.roadcapture.api.exception.social.CCommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;

    @Value("${social.kakao.url.token}")
    private String kakaoTokenUri;

    @Value("${social.kakao.url.profile}")
    private String kakaoProfileUri;

    @Value("${social.kakao.url.unlink}")
    private String kakaoUnlikUri;

    public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        if (kakaoProfileUri == null) throw new CCommunicationException();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(kakaoProfileUri, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new CCommunicationException();
        }
        throw new CCommunicationException();
    }

    public OAuthTokenResponse getKakaoTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", baseUrl + kakaoRedirectUri);
        params.add("code", code);

        if (kakaoTokenUri == null) throw new CCommunicationException();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoTokenUri, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK)
            return gson.fromJson(response.getBody(), OAuthTokenResponse.class);
        throw new CCommunicationException();
    }

    public void kakaoUnlink(String accessToken) {
        if (kakaoUnlikUri == null) throw new CCommunicationException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoUnlikUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) return;
        throw new CCommunicationException();
    }
}
