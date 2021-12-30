package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.exception.EmailDuplicatedException;
import com.untilled.roadcapture.api.exception.EmailLoginFailedException;
import com.untilled.roadcapture.api.exception.UsernameDuplicatedException;
import com.untilled.roadcapture.api.exception.UserNotFoundException;
import com.untilled.roadcapture.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        User createdUser = User.create(signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getUsername());
        duplicateEmail(createdUser);
        duplicateUsername(createdUser);
        userRepository.save(createdUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(EmailLoginFailedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EmailLoginFailedException();
        }
        return new LoginResponse(jwtProvider.createToken(user.getId().toString(), user.getRoles()));
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

    private void duplicateUsername(User createdUser) {
        userRepository.findByUsername(createdUser.getUsername())
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
