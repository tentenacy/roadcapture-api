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

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@ToString
@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
public class Place {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private String name;

    private double latitude;

    private double longitude;

    @Embedded
    private Address address;

    public static Place create(String name, LocalDateTime createdAt, LocalDateTime lastModifiedAt, double latitude, double longitude, Address address) {
        Place place = new Place();
        place.setName(name);
        place.setCreatedAt(createdAt);
        place.setLastModifiedAt(lastModifiedAt);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setAddress(address);
        return place;
    }

    public void update(Place place) {
        this.name = place.getName();
        this.createdAt = place.getCreatedAt();
        this.lastModifiedAt = place.getLastModifiedAt();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.address = place.getAddress();
    }
}
