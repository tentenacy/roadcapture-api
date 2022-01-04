package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.exception.business.ThumbnailNonUniqueException;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.place.Place;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
