package com.untilled.roadcapture.api.client.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverProfile {

    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @ToString
    public static class Response {
        private String nickname;
        private String email;
        private String profile_image;
    }
}
