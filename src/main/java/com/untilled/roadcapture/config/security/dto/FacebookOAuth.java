package com.untilled.roadcapture.config.security.dto;

import org.springframework.stereotype.Component;

@Component
public class FacebookOAuth implements SocialOAuth {
    @Override
    public String getOauthRedirectURL() {
        return null;
    }

    @Override
    public OAuthTokenResponse getTokenInfo(String code) {
        return null;
    }

    @Override
    public SocialProfile getProfile(String accessToken) {
        return null;
    }

    @Override
    public void logout(String accessToken) {

    }
}
