package com.untilled.roadcapture.api.dto.token;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TokenRequest {

    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String refreshToken;
}
