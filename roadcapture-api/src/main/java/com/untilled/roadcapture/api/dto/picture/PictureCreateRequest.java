package com.untilled.roadcapture.api.dto.picture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.domain.picture.Picture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureCreateRequest {

    private boolean isThumbnail;
    @NotNull
    private Integer order;
    private String description;
    @NotNull
    private PlaceCreateRequest place;
    @JsonIgnore
    private String imageUrl;

    public PictureCreateRequest(boolean isThumbnail, String description, PlaceCreateRequest place) {
        this.isThumbnail = isThumbnail;
        this.description = description;
        this.place = place;
    }

    private void setImageUrl(String imageUrl) {}

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PictureCreateRequest(PictureUpdateRequest request) {
        this.isThumbnail = request.isThumbnail();
        this.order = request.getOrder();
        this.imageUrl = request.getImageUrl();
        this.description = request.getDescription();
        this.place = new PlaceCreateRequest(request.getPlace());
    }

    public PictureUpdateRequest toPictureUpdateRequest() {
        return new PictureUpdateRequest(null, this.isThumbnail, this.order, this.description, this.place.toPlaceUpdateRequest());
    }

    public Picture toEntity() {
        return Picture.create(this.isThumbnail, this.order, this.imageUrl, this.description, this.place.toEntity());
    }
}
