package com.untilled.roadcapture.domain.like;

import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseCreationTimeEntity;
import com.untilled.roadcapture.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "likes")
public class Like extends BaseCreationTimeEntity {

    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
