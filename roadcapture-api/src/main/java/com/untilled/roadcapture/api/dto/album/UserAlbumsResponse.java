package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.ThumbnailPictureResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserAlbumsResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String title;
    private ThumbnailPictureResponse thumbnailPicture;
}
