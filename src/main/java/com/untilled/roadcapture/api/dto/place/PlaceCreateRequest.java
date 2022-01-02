package com.untilled.roadcapture.api.dto.place;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceCreateRequest {

    @NotEmpty
    private String name;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private Address address;

    public Place toEntity() {
        return Place.create(this.name, this.latitude, this.longitude, this.address);
    }

    public PlaceCreateRequest(PlaceUpdateRequest request) {
        this.name = request.getName();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.address = request.getAddress();
    }

    public PlaceCreateRequest(String name, double latitude, double longitude, Address address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
