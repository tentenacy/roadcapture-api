package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.base.PageRequest;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import com.untilled.roadcapture.api.exception.EmailDuplicatedException;
import com.untilled.roadcapture.api.exception.NickNameDuplicatedException;
import com.untilled.roadcapture.api.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        User createdUser = User.create(signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getUsername());
        userRepository.findByEmail(createdUser.getEmail())
                .ifPresent(user -> {
                    throw new EmailDuplicatedException();
                });
        userRepository.findByUsername(createdUser.getUsername())
                .ifPresent(user -> {
                    throw new NickNameDuplicatedException();
                });
        userRepository.save(createdUser);
    }

    @Transactional
    public void update(Long userId, UserUpdateRequest userUpdateRequest) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.findByUsername(foundUser.getUsername())
                .ifPresent(user -> {
                    throw new NickNameDuplicatedException();
                });
        foundUser.update(userUpdateRequest.getUsername(), userUpdateRequest.getProfileImageUrl(), userUpdateRequest.getIntroduction(), userUpdateRequest.getAddress());
    }

    @Transactional
    public void delete(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(foundUser);
    }

    public Page<User> findUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest.of());
    }

    public User findOne(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
