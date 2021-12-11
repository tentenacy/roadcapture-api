package com.untilled.roadcapture.domain.follower;

import com.untilled.roadcapture.domain.base.BaseCreationTimeEntity;
import com.untilled.roadcapture.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Follower extends BaseCreationTimeEntity {

    @Id @GeneratedValue
    @Column(name = "follower_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "from_user_fk")
    private User from;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "to_user_fk")
    private User to;

}
