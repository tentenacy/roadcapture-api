package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.social.KakaoProfile;
import com.untilled.roadcapture.api.dto.token.TokenRequest;
import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.exception.CSocialAgreementException;
import com.untilled.roadcapture.api.exception.CUserNotFoundException;
import com.untilled.roadcapture.api.service.KakaoService;
import com.untilled.roadcapture.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;

    @GetMapping
    public Page<UsersResponse> users(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/{userId}")
    public UserResponse user(@PathVariable final Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/details")
    public UserDetailResponse userDetail() {
        return userService.getUserDetail();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Validated SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userService.signup(signupRequest);
    }

    @PostMapping("/social/kakao")
    @ResponseStatus(HttpStatus.CREATED)
    public void signupByKakao(@RequestBody @Validated SignupByKakaoRequest request) {

        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(request.getAccessToken());
        if (ObjectUtils.isEmpty(kakaoProfile)) throw new CUserNotFoundException();
        if (!StringUtils.hasText(kakaoProfile.getKakao_account().getEmail())) {
            kakaoService.kakaoUnlink(request.getAccessToken());
            throw new CSocialAgreementException();
        }

        userService.signup(new SignupRequest(kakaoProfile.getKakao_account().getEmail(), null, kakaoProfile.getProperties().getNickname(),kakaoProfile.getKakao_account().getProfile().getProfile_image_url(), "kakao"));
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse login(@RequestBody @Validated LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/social/kakao/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse loginByKakao(@RequestBody @Validated LoginByKakaoRequest request) {

        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(request.getAccessToken());
        if(ObjectUtils.isEmpty(kakaoProfile)) throw new CUserNotFoundException();

        return userService.loginByKakao(new LoginRequest(kakaoProfile.getKakao_account().getEmail(), null, "kakao"));
    }

    @PostMapping("/token/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse reissue(@RequestBody @Validated TokenRequest request) {
        return userService.reissue(request);
    }

    @PatchMapping("/{userId}")
    public void update(@PathVariable final Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userId, userUpdateRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userService.delete();
    }


}
