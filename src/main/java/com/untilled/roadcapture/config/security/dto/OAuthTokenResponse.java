package com.untilled.roadcapture.config.security.dto;

import lombok.Getter;

@Getter
public class OAuthTokenResponse {

    private String token_type;
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String scope;
    private String id_token;
}
