package com.untilled.roadcapture.domain.picture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureQueryRepository {
}
