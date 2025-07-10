package jiyeon.app.payments.memberProfile.scheduler;

import jiyeon.app.payments.memberProfile.repository.MemberProfileRepository;
import jiyeon.app.payments.memberProfile.service.ProfileViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ViewCountSyncScheduler {

    private final ProfileViewCountService redisService;
    private final MemberProfileRepository memberRepository;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void viewCountsToDB() {
        Map<Long, Long> counts = redisService.getAllViewCounts();
        for (Map.Entry<Long, Long> entry : counts.entrySet()) {
            Long profileId = entry.getKey();
            Long redisCount = entry.getValue();

            memberRepository.findById(profileId)
                    .ifPresent(profile -> {
                        profile.increaseViewCount(redisCount.intValue());
                        redisService.resetViewCount(profileId);
            });
        }
    }
}
