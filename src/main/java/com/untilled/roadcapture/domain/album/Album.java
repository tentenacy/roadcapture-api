package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.comment.Comment;
import com.untilled.roadcapture.domain.like.Like;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

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

    private int viewCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "album", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures = new ArrayList<>();

    public static Album create(String title, String description, List<Picture> pictures, User user) {
        Album album = new Album();
        album.setTitle(title);
        album.setDescription(description);
        pictures.forEach(picture -> album.addPicture(picture));
        album.setUser(user);
        return album;
    }

    public void update(Album album) {
        this.title = album.getTitle();
        this.description = album.getDescription();
    }

    public void addPicture(Picture picture) {
        picture.setAlbum(this);
        this.pictures.add(picture);
    }

    public List<Picture> removeAllPicturesExceptFor(List<Long> ids) {
        List<Picture> picturesToRemove = this.pictures.stream()
                .filter(picture -> !(ids.contains(picture.getId())))
                .collect(Collectors.toList());
        for(Iterator<Picture> itr = picturesToRemove.iterator(); itr.hasNext();) {
            Picture next = itr.next();
            next.setAlbum(null);
            this.pictures.remove(next);
        }
        return picturesToRemove;
    }

    public void addLike(Like like) {
        like.setAlbum(this);
        this.likes.add(like);
    }

    public void removeLike(Like like) {
        like.setAlbum(null);
        this.likes.remove(like);
    }
}
