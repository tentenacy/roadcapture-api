package com.untilled.roadcapture.config.security.dto;

import lombok.*;

@Getter
@ToString
public class GoogleProfile {

    // These six fields are included in all Google ID Tokens.
    private String iss;
    private String sub;
    private String azp;
    private String aud;
    private String iat;
    private String exp;

    // These seven fields are only included when the user has granted the "profile" and
    // "email" OAuth scopes to the application.
    private String email;
    private String emailVerified;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
}
