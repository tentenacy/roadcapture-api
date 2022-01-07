package com.untilled.roadcapture.domain.like;

import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndAlbum(User user, Album album);
}
