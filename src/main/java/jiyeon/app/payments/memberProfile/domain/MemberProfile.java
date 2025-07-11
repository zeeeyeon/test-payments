package jiyeon.app.payments.memberProfile.domain;

import jakarta.persistence.*;
import jiyeon.app.payments.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member_profile",
        indexes = {
                @Index(name = "idx_profile_name_id", columnList = "name ASC, id ASC"),
                @Index(name = "idx_profile_view_count_id", columnList = "view_count DESC, id DESC"),
                @Index(name = "idx_profile_registered_at_id", columnList = "registered_at DESC, id DESC")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    private int point;

    public MemberProfile(String name, int viewCount, LocalDateTime registeredAt) {
        this.name = name;
        this.viewCount = viewCount;
        this.registeredAt = registeredAt;
    }

    public void increaseViewCount(int count) {
        this.viewCount += count;
    }

    public void addPoint(long amount) {
        this.point += amount;
    }
}