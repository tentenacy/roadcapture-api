package com.untilled.roadcapture.domain.picture;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.location.Location;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Picture extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    private String imageUrl;

    private String description;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location marker;

    @Embedded
    private Address address;
}
