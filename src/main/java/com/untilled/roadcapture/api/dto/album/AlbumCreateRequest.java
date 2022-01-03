package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumCreateRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotEmpty
    private String thumbnailUrl;
    @NotNull
    private List<PictureCreateRequest> pictures;

    public AlbumCreateRequest(String title, String description, String thumbnailUrl, List<PictureCreateRequest> pictures) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.pictures = pictures;
    }
}
