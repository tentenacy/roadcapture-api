package com.untilled.roadcapture.api.controller.social;

import com.untilled.roadcapture.api.service.KakaoService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final KakaoService kakaoService;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;

    @GetMapping("/login")
    public String socialLogin(Model model) {

        StringBuilder loginUri = new StringBuilder()
                .append(env.getProperty("social.kakao.url.login"))
                .append("?response_type=code")
                .append("&client_id=").append(kakaoClientId)
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirectUri);
        model.addAttribute("loginUrl", loginUri);

        return "social/login";
    }

    @GetMapping(value = "/redirect")
    public String redirectKakao(Model model, @RequestParam String code) {

        model.addAttribute("authInfo", kakaoService.getKakaoTokenInfo(code));
        return "social/redirectKakao";
    }
}
