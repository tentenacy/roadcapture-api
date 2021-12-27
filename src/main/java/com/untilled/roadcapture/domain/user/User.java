package com.untilled.roadcapture.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.follower.Follower;
import com.untilled.roadcapture.domain.place.Place;
import lombok.*;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String username;

    private String profileImageUrl;

    private String introduction;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Album> albums = new ArrayList<>();

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
    private List<Follower> followers = new ArrayList<>();

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
    private List<Follower> followings = new ArrayList<>();

    public static User create(String email, String password, String username) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        return user;
    }

    public void update(String username, String profileImageUrl, String introduction, Address address) {
        if(StringUtils.hasText(username)) {
            this.username = username;
        }
        if(StringUtils.hasText(profileImageUrl)) {
            this.profileImageUrl = profileImageUrl;
        }
        if(StringUtils.hasText(introduction)) {
            this.introduction = introduction;
        }
        if(!ObjectUtils.isEmpty(address)) {
            this.address = address;
        }
    }

}
