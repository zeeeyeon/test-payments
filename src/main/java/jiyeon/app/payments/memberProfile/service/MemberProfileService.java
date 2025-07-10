package jiyeon.app.payments.memberProfile.service;

import jiyeon.app.payments.memberProfile.dto.MemberProfileListResponse;
import jiyeon.app.payments.memberProfile.dto.SortType;

import java.util.List;

public interface MemberProfileService {
    List<MemberProfileListResponse> getProfiles(SortType sortType, int page, int size);

    void increaseViewCount(Long profileId);
}
