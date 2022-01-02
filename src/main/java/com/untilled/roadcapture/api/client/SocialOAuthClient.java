package com.untilled.roadcapture.api.client;

import com.untilled.roadcapture.api.client.dto.OAuthTokenResponse;
import com.untilled.roadcapture.api.client.dto.SocialProfile;
import com.untilled.roadcapture.util.constant.SocialType;

public interface SocialOAuthClient {

    String getOauthRedirectURL();

    OAuthTokenResponse getTokenInfo(String code);

    SocialProfile getProfile(String accessToken);

    void unlink(String accessToken);

    default SocialType type() {
        if (this instanceof FacebookClient) {
            return SocialType.FACEBOOK;
        } else if (this instanceof GoogleOAuthClient) {
            return SocialType.GOOGLE;
        } else if (this instanceof NaverOAuthClient) {
            return SocialType.NAVER;
        } else if (this instanceof KaKaoOAuthClient) {
            return SocialType.KAKAO;
        } else {
            return null;
        }
    }
}
