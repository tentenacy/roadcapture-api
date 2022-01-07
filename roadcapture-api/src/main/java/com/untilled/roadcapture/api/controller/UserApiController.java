package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.dto.token.TokenRequest;
import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.untilled.roadcapture.api.service.UserService;
import com.untilled.roadcapture.api.service.social.OAuthService;
import com.untilled.roadcapture.api.client.dto.SocialProfile;
import com.untilled.roadcapture.util.constant.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.untilled.roadcapture.api.exception.social.CSocialException.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OAuthService oauthService;

    @GetMapping
    public Page<UsersResponse> users(Pageable pageable, UsersCondition cond) {
        return userService.getUsers(pageable, cond);
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

    @PostMapping("/social/{socialType}")
    @ResponseStatus(HttpStatus.CREATED)
    public void socialSignup(@PathVariable(name = "socialType") SocialType socialType,
                             @RequestBody @Validated SocialSignupRequest request) {

        //구글은 access_token 대신 id_token 값으로 요청
        SocialProfile socialProfile = oauthService.getProfile(socialType, request.getAccessToken());
        if (ObjectUtils.isEmpty(socialProfile)) throw new CUserNotFoundException();
        if (!StringUtils.hasText(socialProfile.getEmail())) {
            oauthService.unlink(socialType, request.getAccessToken());
            throw new CSocialAgreementException();
        }

        userService.signup(new SignupRequest(socialProfile.getEmail(), null, socialProfile.getUsername(), socialProfile.getProfileImageUrl(), socialType.name().toLowerCase()));
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse login(@RequestBody @Validated LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/social/{socialType}/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse socialLogin(@PathVariable(name = "socialType") SocialType socialType,
                                     @RequestBody @Validated SocialLoginRequest request) {

        SocialProfile socialProfile = oauthService.getProfile(socialType, request.getAccessToken());
        if(ObjectUtils.isEmpty(socialProfile)) throw new CUserNotFoundException();

        return userService.socialLogin(new LoginRequest(socialProfile.getEmail(), null, socialType.name().toLowerCase()));
    }

    @PostMapping("/token/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse reissue(@RequestBody @Validated TokenRequest request) {
        return userService.reissue(request);
    }

    @PatchMapping
    public void update(@RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userUpdateRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userService.delete();
    }
}
