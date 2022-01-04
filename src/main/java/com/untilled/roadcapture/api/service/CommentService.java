package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentsResponse;
import com.untilled.roadcapture.api.exception.business.CCommentNotFoundException;
import com.untilled.roadcapture.api.exception.business.CPictureNotFoundException;
import com.untilled.roadcapture.api.exception.business.CUserNotFoundException;
import com.untilled.roadcapture.domain.comment.Comment;
import com.untilled.roadcapture.domain.comment.CommentRepository;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.picture.PictureRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PictureRepository pictureRepository;

    @Transactional
    public Comment create(Long pictureId, CommentCreateRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.create(request.getContent(), getUserIfExists(user.getId()));
        getPictureIfExists(pictureId).addComment(comment);
        return comment;
    }

    @Transactional
    public Comment create(Long userId, Long pictureId, CommentCreateRequest request) {
        Comment comment = Comment.create(request.getContent(), getUserIfExists(userId));
        getPictureIfExists(pictureId).addComment(comment);
        return comment;
    }

    @Transactional
    public void delete(Long commentId) {
        getCommentIfExists(commentId).delete();
    }

    public Page<CommentsResponse> pictureComments(Long pictureId, Pageable pageable) {
        return commentRepository.getPictureComments(pictureId, pageable);
    }

    public Page<CommentsResponse> albumComments(Long albumId, Pageable pageable) {
        return commentRepository.getAlbumComments(albumId, pageable);
    }

    private Comment getCommentIfExists(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CCommentNotFoundException::new);
    }

    private Picture getPictureIfExists(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(CPictureNotFoundException::new);
    }
    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }
}
