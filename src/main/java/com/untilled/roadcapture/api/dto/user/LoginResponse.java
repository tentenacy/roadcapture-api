package com.untilled.roadcapture.api.dto.user;

import lombok.Data;

@Data
public class LoginResponse {

    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
