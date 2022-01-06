package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureCreateRequest;
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
public class TempAlbumCreateRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotNull
    private List<TempPictureCreateRequest> pictures;
}
