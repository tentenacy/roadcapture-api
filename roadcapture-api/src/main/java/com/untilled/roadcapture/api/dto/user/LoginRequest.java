package com.untilled.roadcapture.api.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @Email
    @NotEmpty
    private String email;

    @Length(min = 8, max = 64)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")
    @NotEmpty
    private String password;

    private String provider;

    public LoginRequest(String email, String password, String provider) {
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
