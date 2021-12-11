package com.untilled.roadcapture.domain.place;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Place extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    @Embedded
    private Address address;

}
