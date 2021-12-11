package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsersResponse {
    private Long id;
    private String username;
    private String profileImageUrl;

    public UsersResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
