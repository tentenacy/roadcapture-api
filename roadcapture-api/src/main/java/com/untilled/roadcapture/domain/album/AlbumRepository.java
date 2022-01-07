package com.untilled.roadcapture.domain.album;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long>, AlbumQueryRepository {
}
