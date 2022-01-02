package com.untilled.roadcapture.api.dto.place;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceUpdateRequest {

    @NotEmpty
    private String name;
    private double latitude;
    private double longitude;
    @NotNull
    private Address address;

    public PlaceUpdateRequest(String name, Double latitude, Double longitude, Address address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
