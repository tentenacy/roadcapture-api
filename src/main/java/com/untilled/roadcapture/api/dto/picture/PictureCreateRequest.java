package com.untilled.roadcapture.api.dto.picture;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime createdAt;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime lastModifiedAt;
    private String description;
    @NotNull
    private PlaceCreateRequest place;

    private String imageUrl;

    public PictureCreateRequest(boolean isThumbnail, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String description, PlaceCreateRequest place) {
        this.isThumbnail = isThumbnail;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.description = description;
        this.place = place;
    }

    private void setImageUrl(String imageUrl) {}

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PictureCreateRequest(PictureUpdateRequest request) {
        this.isThumbnail = request.isThumbnail();
        this.createdAt = request.getCreatedAt();
        this.lastModifiedAt = request.getLastModifiedAt();
        this.imageUrl = request.getImageUrl();
        this.description = request.getDescription();
        this.place = new PlaceCreateRequest(request.getPlace());
    }

    public Picture toEntity() {
        return Picture.create(this.isThumbnail, this.createdAt, this.lastModifiedAt, this.imageUrl, this.description, this.place.toEntity());
    }
}
