package com.untilled.roadcapture.domain.location;

import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.base.BaseCreationTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Location extends BaseCreationTimeEntity {

    @Id @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private Double latitude;

    private Double longitude;
}
