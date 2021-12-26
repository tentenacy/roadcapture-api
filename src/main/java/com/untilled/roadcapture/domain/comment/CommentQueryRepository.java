package com.untilled.roadcapture.domain.comment;

import com.untilled.roadcapture.api.dto.comment.CommentsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentQueryRepository {
    Page<CommentsResponse> getPictureComments(Long pictureId, Pageable pageable);
    Page<CommentsResponse> getAlbumComments(Long albumId, Pageable pageable);
}
