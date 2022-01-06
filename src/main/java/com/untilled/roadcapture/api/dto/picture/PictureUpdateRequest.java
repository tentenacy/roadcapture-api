package com.untilled.roadcapture.api.dto.picture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
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
public class PictureUpdateRequest {

    private Long id;
    private boolean isThumbnail;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime createdAt;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime lastModifiedAt;
    private String description;
    private PlaceUpdateRequest place;

    @JsonIgnore
    private String imageUrl;
    @JsonIgnore
    private boolean isImageUrlNotUpdatable = false;

    public PictureUpdateRequest(Long id, boolean isThumbnail, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String description, PlaceUpdateRequest place) {
        this.id = id;
        this.isThumbnail = isThumbnail;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.description = description;
        this.place = place;
    }

    private void setImageUrl(String imageUrl) {}
    private void setImageUrlNotUpdatable(boolean imageUrlNotUpdatable) {}

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void imageUrlNotUpdatable() {
        this.isImageUrlNotUpdatable = true;
    }

    public Picture toEntity() {
        return Picture.create(this.isThumbnail, this.createdAt, this.lastModifiedAt, this.imageUrl, this.description, this.place.toEntity());
    }
}
