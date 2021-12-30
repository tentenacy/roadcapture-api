package com.untilled.roadcapture.api.dto.user;

import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UsersResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
}
