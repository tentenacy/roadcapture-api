package com.untilled.roadcapture.api.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {
    @Email
    @NotEmpty
    private String email;

    @Length(min = 8, max = 64)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")
    @NotEmpty
    private String password;

    @Size(min = 2, max = 12)
    @NotEmpty
    private String username;

    private String provider;

    public SignupRequest(String email, String password, String username, String provider) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.provider = provider;
    }

    public SignupRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
