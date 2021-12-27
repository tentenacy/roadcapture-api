package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.address.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequest {
    private String username;
    private String profileImageUrl;
    private String introduction;
    @Embedded
    private Address address;

    public UserUpdateRequest(String username, String profileImageUrl, String introduction, Address address) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.address = address;
    }
}
