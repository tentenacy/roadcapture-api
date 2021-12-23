package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.comment.Comment;
import com.untilled.roadcapture.domain.like.Like;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class Album extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "album_id")
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private int viewCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private List<Picture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "album", fetch = LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public static Album create(String title, String description, String thumbnailUrl, List<Picture> pictures, User user) {
        Album album = new Album();
        album.setTitle(title);
        album.setDescription(description);
        album.setThumbnailUrl(thumbnailUrl);
        album.pictures.addAll(pictures);
        album.setUser(user);
        return album;
    }

    public void update(String title, String description, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void addPicture(Picture picture) {
        this.pictures.add(picture);
    }
}
