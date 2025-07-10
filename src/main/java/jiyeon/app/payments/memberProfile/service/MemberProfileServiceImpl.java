package jiyeon.app.payments.memberProfile.service;

import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import jiyeon.app.payments.memberProfile.dto.MemberProfileListResponse;
import jiyeon.app.payments.memberProfile.dto.SortType;
import jiyeon.app.payments.memberProfile.repository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MemberProfileListResponse> getProfiles(SortType sortType, int page, int size) {
        return memberProfileRepository.findAllWithSortingAndPaging(sortType, page, size);
    }

    @Override
    @Transactional
    public void increaseViewCount(Long profileId) {
        int updatedCount = memberProfileRepository.incrementViewCount(profileId);

        if (updatedCount == 0) {
            throw new IllegalArgumentException("해당 프로필이 존재하지 않습니다.");
        }
    }
}
