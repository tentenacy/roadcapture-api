package com.untilled.roadcapture.config.security.dto;

import com.untilled.roadcapture.api.exception.social.CCommunicationException;
import com.untilled.roadcapture.api.exception.social.CInvalidSocialTypeException;
import com.untilled.roadcapture.util.constant.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final List<SocialOAuth> socialOAuthList;
    private final HttpServletResponse response;

    public void request(SocialType socialType) {
        SocialOAuth socialOauth = this.findSocialOauthByType(socialType);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            log.error(e.toString());
            throw new CCommunicationException();
        }
    }

    public OAuthTokenResponse getTokenInfo(SocialType socialType, String code) {
        SocialOAuth socialOauth = this.findSocialOauthByType(socialType);
        return socialOauth.getTokenInfo(code);
    }

    public SocialProfile getProfile(SocialType socialType, String accessToken) {
        SocialOAuth socialOauth = this.findSocialOauthByType(socialType);
        return socialOauth.getProfile(accessToken);
    }

    public void unlink(SocialType socialType, String accessToken) {
        SocialOAuth socialOauth = this.findSocialOauthByType(socialType);
        socialOauth.logout(accessToken);
    }

    private SocialOAuth findSocialOauthByType(SocialType socialType) {
        return socialOAuthList.stream()
                .filter(x -> x.type() == socialType)
                .findFirst()
                .orElseThrow(CInvalidSocialTypeException::new);
    }
}
