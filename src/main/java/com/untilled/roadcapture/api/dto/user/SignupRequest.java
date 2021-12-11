package com.untilled.roadcapture.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class SignupRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;
}
