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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempPictureCreateRequest {

    private boolean isThumbnail;
    @NotNull
    private Integer order;
    @Pattern(regexp = "(http(s)?:\\/\\/)([a-z0-9\\w]+\\.*)+[a-z0-9]{2,4}.+")
    private String imageUrl;
    private String description;
    @NotNull
    private PlaceCreateRequest place;

    public TempPictureCreateRequest(TempPictureUpdateRequest request) {
        this.isThumbnail = request.isThumbnail();
        this.order = request.getOrder();
        this.imageUrl = request.getImageUrl();
        this.description = request.getDescription();
        this.place = new PlaceCreateRequest(request.getPlace());
    }

    public Picture toEntity() {
        return Picture.create(this.isThumbnail, this.order, this.imageUrl, this.description, this.place.toEntity());
    }
}
