package com.untilled.roadcapture.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.follower.Follower;
import com.untilled.roadcapture.domain.place.Place;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends BaseTimeEntity implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String username;

    @Column(length = 500)
    private String profileImageUrl;

    @Column(length = 500)
    private String backgroundImageUrl;

    private String introduction;

    @ElementCollection(fetch = EAGER)
    private List<String> roles = new ArrayList<>();

    private String provider;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Album> albums = new ArrayList<>();

    public static User create(String email, String password, String username) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setRoles(Collections.singletonList("ROLE_USER"));
        return user;
    }

    public static User create(String email, String password, String username, String profileImageUrl, String provider) {
        User user = create(email, password, username);
        user.setProfileImageUrl(profileImageUrl);
        user.setProvider(provider);
        return user;
    }

    public void update(String username, String profileImageUrl, String backgroundImageUrl, String introduction, Address address) {
        if(StringUtils.hasText(username)) {
            this.username = username;
        }
        if(StringUtils.hasText(profileImageUrl)) {
            this.profileImageUrl = profileImageUrl;
        }
        if(StringUtils.hasText(backgroundImageUrl)) {
            this.backgroundImageUrl = backgroundImageUrl;
        }
        if(StringUtils.hasText(introduction)) {
            this.introduction = introduction;
        }
        if(!ObjectUtils.isEmpty(address)) {
            this.address = address;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
