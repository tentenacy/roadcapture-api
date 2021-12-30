package com.untilled.roadcapture.api.service;

import com.untilled.roadcapture.api.exception.CAlbumNotFoundException;
import com.untilled.roadcapture.api.exception.CAlreadyLikeException;
import com.untilled.roadcapture.api.exception.CLikeNotFoundException;
import com.untilled.roadcapture.api.exception.CUserNotFoundException;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.album.AlbumRepository;
import com.untilled.roadcapture.domain.like.Like;
import com.untilled.roadcapture.domain.like.LikeRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    //현재 로그인한 사용 정보 가져오기
    //https://coco-log.tistory.com/129?category=912572

    private final LikeRepository likeRepository;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    @Transactional
    public Like create(Long userId, Long albumId) {

        User foundUser = getUserIfExists(userId);
        Album foundAlbum = getAlbumIfExists(albumId);

        validateLikeNotExists(foundUser, foundAlbum);

        Like like = Like.create(foundUser);
        foundAlbum.addLike(like);

        return like;
    }

    @Transactional
    public void delete(Long userId, Long albumId) {
        Album foundAlbum = getAlbumIfExists(albumId);
        Like foundLike = getLikeIfExists(getUserIfExists(userId), foundAlbum);
        foundAlbum.removeLike(foundLike);
    }

    private void validateLikeNotExists(User user, Album album) {
        likeRepository.findByUserAndAlbum(user, album).ifPresent(like -> {
            throw new CAlreadyLikeException();
        });
    }

    private Like getLikeIfExists(User user, Album album) {
        return likeRepository.findByUserAndAlbum(user, album).orElseThrow(CLikeNotFoundException::new);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
    }

    private Album getAlbumIfExists(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(CAlbumNotFoundException::new);
    }
}
