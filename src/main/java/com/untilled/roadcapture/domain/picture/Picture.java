package com.untilled.roadcapture.domain.picture;

import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@ToString
@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
public class Picture extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    private String imageUrl;

    private String description;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place place;

    public static Picture create(String imageUrl, String description, Place place) {
        Picture picture = new Picture();
        picture.setImageUrl(imageUrl);
        picture.setDescription(description);
        picture.setPlace(place);
        return picture;
    }

    public void update(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
