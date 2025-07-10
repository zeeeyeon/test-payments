package jiyeon.app.payments.memberProfile.repository;

import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long>, MemberProfileQueryRepository {
}
