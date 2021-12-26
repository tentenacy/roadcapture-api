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
import java.util.*;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "album", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures = new ArrayList<>();

    public static Album create(String title, String description, String thumbnailUrl, List<Picture> pictures, User user) {
        Album album = new Album();
        album.setTitle(title);
        album.setDescription(description);
        album.setThumbnailUrl(thumbnailUrl);
        pictures.forEach(picture -> album.addPicture(picture));
        album.setUser(user);
        return album;
    }

    public void update(String title, String description, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void addPicture(Picture picture) {
        picture.setAlbum(this);
        this.pictures.add(picture);
    }

    public void removeAllPicturesExceptFor(List<Long> ids) {
        List<Picture> collect = this.pictures.stream()
                .filter(picture -> !(ids.contains(picture.getId())))
                .collect(Collectors.toList());
        for(Iterator<Picture> itr = collect.iterator(); itr.hasNext();) {
            Picture next = itr.next();
            next.setAlbum(null);
            this.pictures.remove(next);
        }
    }
}
