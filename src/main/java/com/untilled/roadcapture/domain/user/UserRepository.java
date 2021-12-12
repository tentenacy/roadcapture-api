package com.untilled.roadcapture.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value = "select o1 from User o1, Album o2 where o2.id = :albumId")
//    User findByAlbumId(@Param("albumId") Long albumId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}

