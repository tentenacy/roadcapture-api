package com.untilled.roadcapture.api.dto.comment;

import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCreateRequest {

    @NotEmpty
    private String content;

    public CommentCreateRequest(String content) {
        this.content = content;
    }
}
