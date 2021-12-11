package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.base.PageRequest;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long signup(SignupRequest signupRequest) {
        User user = User.create(signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getUsername());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Transactional
    public void update(Long userId, UserUpdateRequest userUpdateRequest) {
        User foundUser = userRepository.findById(userId).get();
        foundUser.update(userUpdateRequest.getUsername(), userUpdateRequest.getProfileImageUrl(), userUpdateRequest.getIntroduction(), userUpdateRequest.getAddress());
    }

    @Transactional
    public void delete(Long userId) {
        User foundUser = userRepository.findById(userId).get();
        userRepository.delete(foundUser);
    }

    public Page<User> findUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest.of());
    }

    public User findOne(Long userId) {
        return userRepository.findById(userId).get();
    }
}
