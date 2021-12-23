package com.untilled.roadcapture.api.dto.picture;

import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureCreateRequest {

    private String imageUrl;
    private String description;
    private PlaceCreateRequest place;

    public Picture toEntity() {
        return Picture.create(this.imageUrl, this.description, this.place.toEntity());
    }

    public PictureCreateRequest(PictureUpdateRequest request) {
        this.imageUrl = request.getImageUrl();
        this.description = request.getDescription();
        this.place = new PlaceCreateRequest(request.getPlace());
    }

    public PictureCreateRequest(String imageUrl, String description, PlaceCreateRequest place) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.place = place;
    }
}
