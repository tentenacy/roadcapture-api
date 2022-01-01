package com.untilled.roadcapture.config.security.dto;

import com.google.gson.Gson;
import com.untilled.roadcapture.api.exception.social.CCommunicationException;
import com.untilled.roadcapture.api.exception.social.CSocialTokenValidFailedException;
import com.untilled.roadcapture.config.security.dto.temp.GoogleLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth implements SocialOAuth {

    private final RestTemplate restTemplate;
    private final Gson gson;
    private final WebClient webClient;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${social.google.client-id}")
    private String googleClientId;

    @Value("${social.google.client-secret}")
    private String googleClientSecret;

    @Value("${social.google.redirect}")
    private String googleRedirectUri;

    @Value("${social.google.url.login}")
    private String googleLoginUrl;

    @Value("${social.google.url.token}")
    private String googleTokenUrl;

    @Value("${social.google.url.profile}")
    private String googleProfileUrl;

    @Value("${social.google.url.logout}")
    private String googleLogoutUrl;


    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        //카카오를 제외하고는 parameter에 scope를 지정해줘야 동의됨
        params.put("scope", "profile email");
        params.put("response_type", "code");
        params.put("client_id", googleClientId);
        params.put("redirect_uri", baseUrl + googleRedirectUri);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return googleLoginUrl + "?" + parameterString;
    }

    @Override
    public OAuthTokenResponse getTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("access_type", "offline");
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", baseUrl+ googleRedirectUri);
        params.add("code", code);

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> responseEntity =
                    restTemplate.postForEntity(googleTokenUrl, request, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return gson.fromJson(responseEntity.getBody(), OAuthTokenResponse.class);
            }
        } catch (HttpClientErrorException e) {
            log.error("e={}", e.toString());
        }
        throw new CCommunicationException();
    }

    @Override
    public SocialProfile getProfile(String idToken) {

        GoogleProfile googleProfile = webClient.get()
                .uri(googleProfileUrl, builder -> builder.queryParam("id_token", idToken).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CCommunicationException()))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CCommunicationException()))
                .bodyToMono(GoogleProfile.class)
                .block();

        return new SocialProfile(googleProfile.getEmail(), googleProfile.getName(), googleProfile.getPicture());
    }

    @Override
    public void logout(String accessToken) {
        webClient.post()
                .uri(googleProfileUrl, builder -> builder.queryParam("token", accessToken).build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CCommunicationException()))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CCommunicationException()));
    }
}
