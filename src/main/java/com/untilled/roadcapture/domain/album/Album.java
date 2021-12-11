package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.comment.Comment;
import com.untilled.roadcapture.domain.like.Like;
import com.untilled.roadcapture.domain.location.Location;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
public class Album extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "album_id")
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Integer viewCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_id")
    private List<Picture> pictures = new ArrayList<>();

    @OneToMany(fetch = LAZY)
    @JoinColumn(name = "place_id")
    private List<Place> places;

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;
}
