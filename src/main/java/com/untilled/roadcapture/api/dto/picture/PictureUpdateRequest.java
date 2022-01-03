package com.untilled.roadcapture.api.dto.picture;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.domain.picture.Picture;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureUpdateRequest {

    @NotNull
    private Long id;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime createdAt;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime lastModifiedAt;
    @Pattern(regexp = "(http(s)?:\\/\\/)([a-z0-9\\w]+\\.*)+[a-z0-9]{2,4}.+")
    private String imageUrl;
    private String description;
    private PlaceUpdateRequest place;

    public PictureUpdateRequest(Long id, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String imageUrl, String description, PlaceUpdateRequest place) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.imageUrl = imageUrl;
        this.description = description;
        this.place = place;
    }
}
