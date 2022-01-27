package com.untilled.roadcapture.api.dto.place;

import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embedded;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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

    public PlaceUpdateRequest toPlaceUpdateRequest() {
        return new PlaceUpdateRequest(this.name, this.latitude, this.longitude, this.address);
    }
}
