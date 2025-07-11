package jiyeon.app.payments.point.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class PaymentRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PAYMENT_KEY_PREFIX = "payment:";
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(30);

    public boolean acquirePaymentLock(String orderId) {
        String key = PAYMENT_KEY_PREFIX + orderId;
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(key, "PROCESSING", LOCK_TIMEOUT)
        );
    }

    public void releasePaymentLock(String orderId) {
        String key = PAYMENT_KEY_PREFIX + orderId;
        redisTemplate.delete(key);
    }
}
