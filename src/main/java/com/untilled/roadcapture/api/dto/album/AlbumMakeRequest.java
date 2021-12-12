package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class AlbumMakeRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotEmpty
    private String thumbnailUrl;
    @NotNull
    private List<Picture> pictures;
    private List<Place> places;
    @NotNull
    private Long userId;
}
