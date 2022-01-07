package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureUpdateRequest;
import com.untilled.roadcapture.domain.album.Album;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAlbumUpdateRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotNull
    private List<TempPictureUpdateRequest> pictures;

    public Album toEntity() {
        return Album.create(this.title, this.description, this.pictures.stream().map(picture -> picture.toEntity()).collect(Collectors.toList()), null);
    }
}
