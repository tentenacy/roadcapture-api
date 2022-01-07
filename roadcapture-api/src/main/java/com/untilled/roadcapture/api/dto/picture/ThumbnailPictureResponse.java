package com.untilled.roadcapture.api.dto.picture;

import com.untilled.roadcapture.api.dto.place.PlaceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ThumbnailPictureResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String imageUrl;
    private PlaceResponse place;
}
