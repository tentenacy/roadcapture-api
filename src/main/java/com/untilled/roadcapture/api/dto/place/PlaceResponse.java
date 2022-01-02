package com.untilled.roadcapture.api.dto.place;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PlaceResponse {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Address address;
}
