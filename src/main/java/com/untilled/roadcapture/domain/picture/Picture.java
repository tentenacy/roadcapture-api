package com.untilled.roadcapture.domain.picture;

import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.comment.Comment;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@ToString
@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
public class Picture {

    @Id @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private String imageUrl;

    private String description;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @OneToMany(mappedBy = "picture", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private boolean isThumbnail;

    public static Picture create(boolean isThumbnail, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String imageUrl, String description, Place place) {
        Picture picture = new Picture();
        picture.createdAt = createdAt;
        picture.lastModifiedAt = lastModifiedAt;
        picture.isThumbnail = isThumbnail;
        picture.imageUrl = imageUrl;
        picture.description = description;
        picture.place = place;
        return picture;
    }

    public void update(Picture picture) {
        this.isThumbnail = picture.isThumbnail();
        this.createdAt = picture.getCreatedAt();
        this.lastModifiedAt = picture.getLastModifiedAt();
        this.imageUrl = picture.getImageUrl();
        this.description = picture.getDescription();
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void addComment(Comment comment) {
        comment.setPicture(this);
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comment.setPicture(null);
        this.comments.remove(comment);
    }
}
