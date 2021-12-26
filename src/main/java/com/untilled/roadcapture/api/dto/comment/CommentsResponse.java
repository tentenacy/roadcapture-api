package com.untilled.roadcapture.api.dto.comment;

import com.untilled.roadcapture.api.dto.user.UsersResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsResponse {

    private Long id;
    private Long pictureId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String content;
    private UsersResponse user;

    public CommentsResponse(Long id, Long pictureId, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String content, UsersResponse user) {
        this.id = id;
        this.pictureId = pictureId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.content = content;
        this.user = user;
    }
}
