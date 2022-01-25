package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.token.TokenRequest;
import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException.CAlreadySignedupException;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException.CEmailLoginFailedException;
import com.untilled.roadcapture.config.security.JwtProvider;
import com.untilled.roadcapture.domain.token.RefreshToken;
import com.untilled.roadcapture.domain.token.RefreshTokenRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.untilled.roadcapture.api.exception.security.CTokenException.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignupRequest request) {
        User user = User.create(request.getEmail(), request.getPassword(), request.getUsername(), request.getProfileImageUrl(), request.getProvider());
        userRepository.findByEmail(user.getEmail())
                .ifPresentOrElse(
                        foundUser -> {
                            throw new CAlreadySignedupException();
                        },
                        () -> userRepository.save(user));
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(CEmailLoginFailedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CEmailLoginFailedException();
        }
        refreshTokenRepository.findByKey(user.getId()).ifPresent(refreshTokenRepository::delete);
        TokenResponse tokenResponse = jwtProvider.createToken(user.getId().toString(), user.getRoles());
        refreshTokenRepository.save(RefreshToken.create(user.getId(), tokenResponse.getRefreshToken()));
        return tokenResponse;
    }

    @Transactional
    public TokenResponse socialLogin(LoginRequest request) {
        User user = userRepository.findByEmailAndProvider(request.getEmail(), request.getProvider())
                .orElseThrow(CUserNotFoundException::new);
        refreshTokenRepository.findByKey(user.getId()).ifPresent(refreshTokenRepository::delete);
        TokenResponse tokenResponse = jwtProvider.createToken(user.getId().toString(), user.getRoles());
        refreshTokenRepository.save(RefreshToken.create(user.getId(), tokenResponse.getRefreshToken()));
        return tokenResponse;
    }

    /**
     * TokenRequest를 통해 액세스 토큰 재발급 요청
     * * 리프레시 토큰 만료 검증 -> 만료 시 재로그인 요청
     */
    @Transactional
    public TokenResponse reissue(TokenRequest request) {

        //리프레시 토큰 만료
        if(!jwtProvider.validationToken(request.getRefreshToken())) {
            throw new CRefreshTokenException();
        }

        String accessToken = request.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User foundUser = getUserThrowable(((User)authentication.getPrincipal()).getId());

        //리프레시 토큰 없음
        RefreshToken refreshToken = refreshTokenRepository.findByKey(foundUser.getId())
                .orElseThrow(CRefreshTokenException::new);

        //리프레시 토큰 불일치
        if(!refreshToken.getToken().equals(request.getRefreshToken())) {
            throw new CRefreshTokenException();
        }

        TokenResponse newCreatedToken = jwtProvider.createToken(foundUser.getId().toString(), foundUser.getRoles());
        refreshToken.update(newCreatedToken.getRefreshToken());

        return newCreatedToken;
    }

    @Transactional
    public void update(UserUpdateRequest userUpdateRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        getUserThrowable(user.getId()).update(userUpdateRequest.getUsername(), userUpdateRequest.getProfileImageUrl(), userUpdateRequest.getBackgroundImageUrl(), userUpdateRequest.getIntroduction(), userUpdateRequest.getAddress());
    }

    @Transactional
    public void update(Long userId, UserUpdateRequest userUpdateRequest) {
        getUserThrowable(userId).update(userUpdateRequest.getUsername(), userUpdateRequest.getProfileImageUrl(), userUpdateRequest.getBackgroundImageUrl(), userUpdateRequest.getIntroduction(), userUpdateRequest.getAddress());
    }

    @Transactional
    public void delete() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.deleteByKey(user.getId());
        userRepository.delete(getUserThrowable(user.getId()));
    }

    public Page<UsersResponse> getUsers(Pageable pageable, UsersCondition cond) {
        return userRepository.search(pageable, cond);
    }

    public StudioUserResponse getStudioUser(Long studioUserId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = getUserThrowable(user.getId());
        return userRepository.studioUser(foundUser.getId(), studioUserId).orElseThrow(CUserNotFoundException::new);
    }

    public MyStudioUserResponse getMyStudioUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getStudioUser(user.getId()).toMyStudioUserResponse();
    }

    public UserDetailResponse getUserDetail() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = getUserThrowable(user.getId());
        return new UserDetailResponse(foundUser);
    }

    private User getUserThrowable(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }
}
