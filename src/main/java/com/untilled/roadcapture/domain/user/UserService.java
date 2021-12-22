package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.base.PageRequest;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.api.exception.EmailDuplicatedException;
import com.untilled.roadcapture.api.exception.UsernameDuplicatedException;
import com.untilled.roadcapture.api.exception.UserNotFoundException;
import com.untilled.roadcapture.domain.address.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signup(SignupRequest signupRequest) {
        User createdUser = User.create(signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getUsername());
        duplicateEmail(createdUser);
        duplicateUsername(createdUser);
        return userRepository.save(createdUser);
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
        return userRepository.findAll(pageable).map(UsersResponse::new);
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
