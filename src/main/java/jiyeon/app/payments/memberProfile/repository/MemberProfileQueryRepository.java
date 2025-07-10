package jiyeon.app.payments.memberProfile.repository;

import jiyeon.app.payments.memberProfile.dto.MemberProfileListResponse;
import jiyeon.app.payments.memberProfile.dto.SortType;

import java.util.List;

public interface MemberProfileQueryRepository {
    List<MemberProfileListResponse> searchProfilesBySortAndPage(SortType sortType, int page, int size);
}
