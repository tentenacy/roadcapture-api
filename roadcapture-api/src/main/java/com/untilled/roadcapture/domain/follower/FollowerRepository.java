package com.untilled.roadcapture.domain.follower;

import com.untilled.roadcapture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long>, FollowerQueryRepository {
}
