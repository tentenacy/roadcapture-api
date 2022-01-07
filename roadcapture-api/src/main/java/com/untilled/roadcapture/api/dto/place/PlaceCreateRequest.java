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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime createdAt;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime lastModifiedAt;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private Address address;

    public Place toEntity() {
        return Place.create(this.name, this.createdAt, this.lastModifiedAt, this.latitude, this.longitude, this.address);
    }

    public PlaceCreateRequest(PlaceUpdateRequest request) {
        this.name = request.getName();
        this.createdAt = request.getCreatedAt();
        this.lastModifiedAt = request.getLastModifiedAt();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.address = request.getAddress();
    }

    public PlaceUpdateRequest toPlaceUpdateRequest() {
        return new PlaceUpdateRequest(this.name, this.createdAt, this.lastModifiedAt, this.latitude, this.longitude, this.address);
    }
}
