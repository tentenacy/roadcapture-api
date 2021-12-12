package com.untilled.roadcapture.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class SignupRequest {
    @Email
    private String email;
    @Length(min = 8, max = 64)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")
    private String password;
    @Size(min = 2, max = 12)
    private String username;

    public SignupRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
