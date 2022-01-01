package com.untilled.roadcapture.config.security.dto;

import com.untilled.roadcapture.util.constant.SocialType;

public interface SocialOAuth {

    String getOauthRedirectURL();

    OAuthTokenResponse getTokenInfo(String code);

    SocialProfile getProfile(String accessToken);

    void unlink(String accessToken);

    default SocialType type() {
        if (this instanceof FacebookOAuth) {
            return SocialType.FACEBOOK;
        } else if (this instanceof GoogleOAuth) {
            return SocialType.GOOGLE;
        } else if (this instanceof NaverOAuth) {
            return SocialType.NAVER;
        } else if (this instanceof KaKaoOAuth) {
            return SocialType.KAKAO;
        } else {
            return null;
        }
    }
}
