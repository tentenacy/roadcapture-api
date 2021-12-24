package com.untilled.roadcapture.api.dto.picture;

import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.domain.picture.Picture;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureUpdateRequest {

    @NotNull
    private Long id;
    @NotEmpty
    private String imageUrl;
    private String description;
    private PlaceUpdateRequest place;

    public PictureUpdateRequest(Long id, String imageUrl, String description, PlaceUpdateRequest place) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.place = place;
    }
}
