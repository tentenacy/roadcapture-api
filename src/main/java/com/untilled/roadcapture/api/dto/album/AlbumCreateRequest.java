package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumCreateRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotNull
    private List<PictureCreateRequest> pictures;
}
