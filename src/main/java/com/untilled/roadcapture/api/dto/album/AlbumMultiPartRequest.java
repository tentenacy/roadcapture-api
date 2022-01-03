package com.untilled.roadcapture.api.dto.album;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumMultiPartRequest {

    private String data;
    private Map<String, MultipartFile> images;
}
