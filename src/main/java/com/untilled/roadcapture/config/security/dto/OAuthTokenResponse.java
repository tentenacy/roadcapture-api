package com.untilled.roadcapture.config.security.dto;

import lombok.Getter;

@Getter
public class OAuthTokenResponse {

    private String access_token;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String token_type;
    private Integer expires_in;
    private String scope;
    private String error;
    private String error_description;

    //구글에만 해당
    private String id_token;
}
