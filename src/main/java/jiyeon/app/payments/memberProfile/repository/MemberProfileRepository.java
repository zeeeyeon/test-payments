package jiyeon.app.payments.memberProfile.repository;

import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long>, MemberProfileQueryRepository {

    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query("update MemberProfile m set m.viewCount = m.viewCount + 1 where m.id = :id")
    int incrementViewCount(@Param("id") Long id);
}
