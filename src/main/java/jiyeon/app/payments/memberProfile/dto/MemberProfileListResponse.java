package jiyeon.app.payments.memberProfile.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record MemberProfileListResponse(
        String name,
        int viewCount,
        LocalDateTime registeredAt
) {
    @QueryProjection
    public MemberProfileListResponse {}
}
