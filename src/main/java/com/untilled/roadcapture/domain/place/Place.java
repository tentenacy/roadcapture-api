package com.untilled.roadcapture.domain.place;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseTimeEntity;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@ToString
@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    private double latitude;

    private double longitude;

    @Embedded
    private Address address;

    public static Place create(String name, double latitude, double longitude, Address address) {
        Place place = new Place();
        place.setName(name);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setAddress(address);
        return place;
    }

    public void update(String name, double latitude, double longitude, Address address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
