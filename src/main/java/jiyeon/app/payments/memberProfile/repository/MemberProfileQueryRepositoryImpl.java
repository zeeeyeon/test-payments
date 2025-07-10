package jiyeon.app.payments.memberProfile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.app.payments.memberProfile.dto.MemberProfileListResponse;
import jiyeon.app.payments.global.memberProfile.dto.QMemberProfileListResponse;
import jiyeon.app.payments.memberProfile.dto.SortType;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static jiyeon.app.payments.global.memberProfile.domain.QMemberProfile.memberProfile;

@RequiredArgsConstructor
public class MemberProfileQueryRepositoryImpl implements MemberProfileQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberProfileListResponse> findAllWithSortingAndPaging(SortType sortType, int page, int size) {
        return queryFactory
                .select(new QMemberProfileListResponse(
                        memberProfile.name,
                        memberProfile.viewCount,
                        memberProfile.registeredAt
                ))
                .from(memberProfile)
                .orderBy(sortType.toOrderSpecifier(memberProfile))
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }
}
