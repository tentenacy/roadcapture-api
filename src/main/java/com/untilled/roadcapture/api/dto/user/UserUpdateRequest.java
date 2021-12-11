package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.address.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;

@Data
@NoArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String profileImageUrl;
    private String introduction;
    @Embedded
    private Address address;
}
