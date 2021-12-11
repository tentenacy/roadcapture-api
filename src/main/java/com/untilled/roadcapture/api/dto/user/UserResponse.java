package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private String introduction;
    private List<Place> preferencePlaces;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();
        this.introduction = user.getIntroduction();
        this.preferencePlaces = user.getPreferencePlaces();
    }
}
