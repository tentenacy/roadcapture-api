package com.untilled.roadcapture.api.dto.token;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class TokenResponse {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;
}
