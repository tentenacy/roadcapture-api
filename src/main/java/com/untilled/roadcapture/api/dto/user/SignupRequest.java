package com.untilled.roadcapture.api.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
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


    @Pattern(regexp = "(http(s)?:\\/\\/)([a-z0-9\\w]+\\.*)+[a-z0-9]{2,4}")
    private String profileImageUrl;

    @Pattern(regexp = "^kakao$|^facebook$|^naver$|^google$")
    private String provider;

    public SignupRequest(String email, String password, String username, String profileImageUrl, String provider) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public SignupRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
