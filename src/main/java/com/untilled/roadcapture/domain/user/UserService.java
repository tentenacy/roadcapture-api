package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.token.TokenRequest;
import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.exception.*;
import com.untilled.roadcapture.config.security.JwtProvider;
import com.untilled.roadcapture.domain.token.RefreshToken;
import com.untilled.roadcapture.domain.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

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
    public void signup(SignupRequest signupRequest) {
        User user = User.create(signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getUsername());
        duplicateEmail(user);
        duplicateUsername(user);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(EmailLoginFailedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EmailLoginFailedException();
        }
        //기존에 리프레시 토큰이 있으면
        //삭제 or 예외?
        refreshTokenRepository.findByKey(user.getId()).ifPresent(a -> {
            refreshTokenRepository.delete(a);
        });
        TokenResponse tokenResponse = jwtProvider.createToken(user.getId().toString(), user.getRoles());
        RefreshToken refreshToken = RefreshToken.create(user.getId(), tokenResponse.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
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
            throw new RefreshTokenException();
        }

        String accessToken = request.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User foundUser = getUserIfExists(((User)authentication.getPrincipal()).getId());

        //리프레시 토큰 없음
        RefreshToken refreshToken = refreshTokenRepository.findByKey(foundUser.getId())
                .orElseThrow(RefreshTokenException::new);

        //리프레시 토큰 불일치
        if(!refreshToken.getToken().equals(request.getRefreshToken())) {
            throw new RefreshTokenException();
        }

        TokenResponse newCreatedToken = jwtProvider.createToken(foundUser.getId().toString(), foundUser.getRoles());
        refreshToken.updateToken(newCreatedToken.getRefreshToken());

        return newCreatedToken;
    }

    @Transactional
    public void update(Long userId, UserUpdateRequest userUpdateRequest) {
        User foundUser = getUserIfExists(userId);
        foundUser.update(userUpdateRequest.getUsername(), userUpdateRequest.getProfileImageUrl(), userUpdateRequest.getIntroduction(), userUpdateRequest.getAddress());
    }

    @Transactional
    public void delete(Long userId) {
        User foundUser = getUserIfExists(userId);
        userRepository.delete(foundUser);
    }

    public Page<UsersResponse> getUsers(Pageable pageable) {
//        return userRepository.findAll(pageable).map(UsersResponse::new);
        return userRepository.search(pageable);
    }

    public UserResponse getUser(Long userId) {
        return new UserResponse(getUserIfExists(userId));
    }

    public UserDetailResponse getUserDetail(Long userId) {
        return new UserDetailResponse(getUserIfExists(userId));
    }

    private void duplicateUsername(User userToCreate) {
        userRepository.findByUsername(userToCreate.getUsername())
                .ifPresent(user -> {
                    throw new UsernameDuplicatedException();
                });
    }

    private void duplicateEmail(User createdUser) {
        userRepository.findByEmail(createdUser.getEmail())
                .ifPresent(user -> {
                    throw new EmailDuplicatedException();
                });
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
