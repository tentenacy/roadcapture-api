package com.untilled.roadcapture.api.dto.picture;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureCreateRequest {

    @NotEmpty
    private String createdAt;
    @NotEmpty
    private String lastModifiedAt;
    @Pattern(regexp = "(http(s)?:\\/\\/)([a-z0-9\\w]+\\.*)+[a-z0-9]{2,4}.+")
    private String imageUrl;
    private String description;
    @NotNull
    private PlaceCreateRequest place;

    public Picture toEntity() {
        return Picture.create(this.imageUrl, this.description, this.place.toEntity());
    }

    public PictureCreateRequest(PictureUpdateRequest request) {
        this.imageUrl = request.getImageUrl();
        this.description = request.getDescription();
        this.place = new PlaceCreateRequest(request.getPlace());
    }

    public PictureCreateRequest(String createdAt, String lastModifiedAt, String imageUrl, String description, PlaceCreateRequest place) {
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.imageUrl = imageUrl;
        this.description = description;
        this.place = place;
    }
}
