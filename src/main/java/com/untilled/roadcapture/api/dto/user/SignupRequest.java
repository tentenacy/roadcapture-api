package com.untilled.roadcapture.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class SignupRequest {
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;
    @Size(min = 2, max = 12)
    private String username;

    public SignupRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
