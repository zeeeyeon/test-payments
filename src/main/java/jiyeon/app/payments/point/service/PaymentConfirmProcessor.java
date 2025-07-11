package jiyeon.app.payments.point.service;

import jiyeon.app.payments.point.domain.PointCharge;
import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;
import jiyeon.app.payments.point.exception.PaymentLockException;
import jiyeon.app.payments.point.repository.PaymentRedisRepository;
import jiyeon.app.payments.point.repository.PointChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentConfirmProcessor {

    private final PointChargeRepository pointChargeRepository;
    private final PaymentRequester paymentRequester;
    private final PaymentRedisRepository paymentRedisRepository;

    @Transactional
    public PaymentConfirmResult process(PaymentConfirmRequest request) {
        try {
            if (!paymentRedisRepository.acquirePaymentLock(request.orderId())) {
                throw new PaymentLockException("이미 처리중인 결제입니다.");
            }

            PointCharge charge = pointChargeRepository.findByOrderId(request.orderId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

            if (charge.isAlreadyApproved()) {
                return new PaymentConfirmResult(
                        charge.getOrderId(),
                        charge.getPaymentKey(),
                        charge.getMethod(),
                        charge.getStatus().name()
                );
            }

            if (!charge.getAmount().equals(request.amount())) {
                throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
            }

            PaymentConfirmResult result = paymentRequester.requestConfirm(request);
            charge.markSuccess(request.paymentKey(), result.method());
            charge.getMember().addPoint(charge.getAmount());

            return result;
        } finally {
            paymentRedisRepository.releasePaymentLock(request.orderId());
        }
    }
}
