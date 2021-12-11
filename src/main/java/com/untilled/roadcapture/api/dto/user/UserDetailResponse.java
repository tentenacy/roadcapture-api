package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embedded;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailResponse {
    private Long id;
    private String email;
    private String username;
    private String profileImageUrl;
    private String introduction;
    @Embedded
    private Address address;
    private List<Place> preferencePlaces;

    public UserDetailResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();
        this.introduction = user.getIntroduction();
        this.address = user.getAddress();
        this.preferencePlaces = user.getPreferencePlaces();
    }
}
