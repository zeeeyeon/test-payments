package jiyeon.app.payments.memberProfile.repository;

import jiyeon.app.payments.memberProfile.dto.MemberProfileListResponse;
import jiyeon.app.payments.memberProfile.dto.SortType;

import java.util.List;

public interface MemberProfileQueryRepository {
    List<MemberProfileListResponse> findAllWithSortingAndPaging(SortType sortType, int page, int size);
}
