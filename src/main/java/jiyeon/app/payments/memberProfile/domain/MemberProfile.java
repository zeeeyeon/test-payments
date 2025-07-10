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
                @Index(name = "profile_name", columnList = "name"),
                @Index(name = "profile_view_count", columnList = "viewCount"),
                @Index(name = "profile_registered_at", columnList = "registeredAt")
        })
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

    public MemberProfile(String name, int viewCount, LocalDateTime registeredAt) {
        this.name = name;
        this.viewCount = viewCount;
        this.registeredAt = registeredAt;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}

