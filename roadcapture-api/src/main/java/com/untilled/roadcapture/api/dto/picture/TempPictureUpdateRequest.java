package com.untilled.roadcapture.api.dto.picture;

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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempPictureUpdateRequest {

    private Long id;
    private boolean isThumbnail;
    @NotNull
    private Integer order;
    @Pattern(regexp = "(http(s)?:\\/\\/)([a-z0-9\\w]+\\.*)+[a-z0-9]{2,4}.+")
    private String imageUrl;
    private String description;
    private PlaceUpdateRequest place;

    public Picture toEntity() {
        return Picture.create(this.isThumbnail, this.order, this.imageUrl, this.description, this.place.toEntity());
    }
}
