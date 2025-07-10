package jiyeon.app.payments.memberProfile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileViewCountService {

    private static final String VIEW_COUNT_KEY_PREFIX = "profile:viewcount:";
    private final RedisTemplate<String, String> redisTemplate;

    public void increaseViewCount(Long profileId) {
        String key = VIEW_COUNT_KEY_PREFIX + profileId;
        redisTemplate.opsForValue().increment(key);
    }

    public Map<Long, Long> getAllViewCounts() {
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*");
        Map<Long, Long> result = new HashMap<>();
        if (keys != null) {
            for (String key : keys) {
                String StringId = key.replace(VIEW_COUNT_KEY_PREFIX, "");
                Long id = Long.parseLong(StringId);
                Long count = Long.parseLong(redisTemplate.opsForValue().get(key));
                result.put(id, count);
            }
        }
        return result;
    }

    public void resetViewCount(Long profileId) {
        redisTemplate.delete(VIEW_COUNT_KEY_PREFIX + profileId);
    }

    public Long getViewCount(Long profileId) {
        String key = VIEW_COUNT_KEY_PREFIX + profileId;
        String count = redisTemplate.opsForValue().get(key);
        return count == null ? 0 : Long.parseLong(count);
    }
}
