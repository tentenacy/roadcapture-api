package com.untilled.roadcapture.api.dto.album;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class AlbumMakeRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotEmpty
    private String thumbnailUrl;
    private Long userId;
}
