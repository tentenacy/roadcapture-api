package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.address.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserUpdateRequest {
    @Size(min = 2, max = 12)
    private String username;
    private String profileImageUrl;
    private String introduction;
    @Embedded
    private Address address;
}
