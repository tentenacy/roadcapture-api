package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentsResponse;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CCommentNotFoundException;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CPictureNotFoundException;
import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserNotFoundException;
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
        Comment comment = Comment.create(request.getContent(), getUserThrowable(user.getId()));
        getPictureThrowable(pictureId).addComment(comment);
        return comment;
    }

    @Transactional
    public Comment create(Long userId, Long pictureId, CommentCreateRequest request) {
        Comment comment = Comment.create(request.getContent(), getUserThrowable(userId));
        getPictureThrowable(pictureId).addComment(comment);
        return comment;
    }

    @Transactional
    public void delete(Long commentId) {
        getCommentThrowable(commentId).delete();
    }

    public Page<CommentsResponse> pictureComments(Long pictureId, Pageable pageable) {
        return commentRepository.getPictureComments(pictureId, pageable);
    }

    public Page<CommentsResponse> albumComments(Long albumId, Pageable pageable) {
        return commentRepository.getAlbumComments(albumId, pageable);
    }

    private Comment getCommentThrowable(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CCommentNotFoundException::new);
    }

    private Picture getPictureThrowable(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(CPictureNotFoundException::new);
    }
    private User getUserThrowable(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }
}
