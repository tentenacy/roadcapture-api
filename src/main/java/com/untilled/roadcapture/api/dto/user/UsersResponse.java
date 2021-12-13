package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersResponse {
    private Long id;
    private String username;
    private String profileImageUrl;

    public UsersResponse(Long id, String username, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public UsersResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
