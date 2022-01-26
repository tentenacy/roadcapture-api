package com.untilled.roadcapture.domain.follower;

import com.untilled.roadcapture.domain.base.BaseCreationTimeEntity;
import com.untilled.roadcapture.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="follower_uk",
                        columnNames = {"from_user_fk", "to_user_fk"}
                )
        }
)
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

    public static Follower create(User from, User to) {
        Follower follower = new Follower();
        follower.setFrom(from);
        follower.setTo(to);
        return follower;
    }
}
