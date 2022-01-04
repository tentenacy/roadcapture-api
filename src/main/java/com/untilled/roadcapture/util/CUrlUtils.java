package com.untilled.roadcapture.util;

import org.springframework.web.util.UriComponentsBuilder;

public class CUrlUtils {
    public static String extractFileNameFrom(String url) {
        return UriComponentsBuilder.fromUriString(url).build().getPath().substring(1);
    }
}
