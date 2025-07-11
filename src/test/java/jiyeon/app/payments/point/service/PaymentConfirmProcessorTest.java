package jiyeon.app.payments.point.service;

import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import jiyeon.app.payments.point.domain.PointCharge;
import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;
import jiyeon.app.payments.point.exception.PaymentLockException;
import jiyeon.app.payments.point.repository.PointChargeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentConfirmProcessorTest {

    @Autowired
    private PaymentConfirmProcessor paymentConfirmProcessor;

    @MockBean
    private PointChargeRepository pointChargeRepository;

    @MockBean
    private PaymentRequester paymentRequester;

    @Test
    public void testConcurrentPaymentRequests() throws Exception {
        // Given
        String orderId = "test-order-123";
        long amount = 10000L;

        MemberProfile mockMember = mock(MemberProfile.class);
        PointCharge mockCharge = PointCharge.createPending(mockMember, amount, orderId);

        when(pointChargeRepository.findByOrderId(orderId))
            .thenReturn(Optional.of(mockCharge));

        when(paymentRequester.requestConfirm(any()))
            .thenReturn(new PaymentConfirmResult(orderId, "test-payment-key", "CARD", "DONE"));

        PaymentConfirmRequest request = new PaymentConfirmRequest(
            orderId,
            "test-payment-key",
            amount
        );

        // When: 동시에 3개의 동일한 결제 요청을 보냄
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<Object>[] futures = new CompletableFuture[3];

        for (int i = 0; i < 3; i++) {
            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    return paymentConfirmProcessor.process(request);
                } catch (PaymentLockException e) {
                    return e;
                }
            }, executorService);
        }

        CompletableFuture.allOf(futures).join();

        // Then: 하나의 요청만 성공하고 나머지는 PaymentLockException이 발생해야 함
        int successCount = 0;
        int lockExceptionCount = 0;

        for (CompletableFuture<Object> future : futures) {
            Object result = future.get();
            if (result instanceof PaymentConfirmResult) {
                successCount++;
            } else if (result instanceof PaymentLockException) {
                lockExceptionCount++;
            }
        }

        assertEquals(1, successCount, "Only one payment should succeed");
        assertEquals(2, lockExceptionCount, "Two requests should fail with PaymentLockException");

        executorService.shutdown();
    }
}
