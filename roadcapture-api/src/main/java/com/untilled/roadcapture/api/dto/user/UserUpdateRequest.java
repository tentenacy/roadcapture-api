package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.address.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequest {
    private String username;
    @Pattern(regexp = "(http(s)?:\\/\\/)([a-z0-9\\w]+\\.*)+[a-z0-9]{2,4}")
    private String profileImageUrl;
    private String introduction;
    private Address address;

    public UserUpdateRequest(String username, String profileImageUrl, String introduction, Address address) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.address = address;
    }
}
