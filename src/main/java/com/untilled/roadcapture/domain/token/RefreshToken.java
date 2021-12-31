package com.untilled.roadcapture.domain.token;

import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "token_key")
    private Long key;

    private String token;

    public void update(String token) {
        this.token = token;
    }

    public static RefreshToken create(Long key, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setKey(key);
        refreshToken.setToken(token);
        return refreshToken;
    }
}
