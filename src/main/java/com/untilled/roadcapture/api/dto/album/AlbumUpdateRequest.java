package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumUpdateRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotEmpty
    private String thumbnailUrl;
    @NotNull
    private List<PictureUpdateRequest> pictures;

    public AlbumUpdateRequest(String title, String description, String thumbnailUrl, List<PictureUpdateRequest> pictures) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.pictures = pictures;
    }
}
