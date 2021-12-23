package com.untilled.roadcapture.api.dto.picture;

import com.untilled.roadcapture.api.dto.place.PlaceResponse;
import com.untilled.roadcapture.domain.picture.Picture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PictureResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String imageUrl;
    private String description;
    private PlaceResponse place;

    public PictureResponse(Picture picture) {
        this.id = picture.getId();
        this.createdAt = picture.getCreatedAt();
        this.lastModifiedAt = picture.getLastModifiedAt();
        this.imageUrl = picture.getImageUrl();
        this.description = picture.getDescription();
        this.place = new PlaceResponse(picture.getPlace());
    }
}
