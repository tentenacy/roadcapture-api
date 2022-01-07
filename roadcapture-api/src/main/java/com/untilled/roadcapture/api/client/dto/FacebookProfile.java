package com.untilled.roadcapture.api.client.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FacebookProfile {

    private String id;
    private String email;
    private String name;
    private Picture picture;

    @Getter
    @ToString
    public static class Picture {

        private Data data;

        @Getter
        @ToString
        public static class Data {

            private String url;
        }
    }
}
