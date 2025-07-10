package jiyeon.app.payments.point.service;

import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import jiyeon.app.payments.memberProfile.repository.MemberProfileRepository;
import jiyeon.app.payments.point.domain.PointCharge;
import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;
import jiyeon.app.payments.point.repository.PointChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PointChargeServiceImpl implements PointChargeService {

    private final MemberProfileRepository memberProfileRepository;
    private final PointChargeRepository pointChargeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String SECRET_KEY = "test_sk_eqRGgYO1r5KvJNqOPWja3QnN2Eya";

    @Override
    @Transactional
    public Long createCharge(Long memberId, Long amount, String orderId) {
        MemberProfile member = memberProfileRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        PointCharge charge = PointCharge.createPending(member, amount, orderId);
        pointChargeRepository.save(charge);
        return charge.getId();
    }

    @Override
    @Transactional
    public PaymentConfirmResult confirmCharge(PaymentConfirmRequest request) {
        PointCharge charge = pointChargeRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (!charge.getAmount().equals(request.amount())) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String encodedKey = Base64.getEncoder()
                .encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedKey);

        HttpEntity<PaymentConfirmRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<PaymentConfirmResult> response = restTemplate.exchange(
                TOSS_CONFIRM_URL,
                HttpMethod.POST,
                entity,
                PaymentConfirmResult.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            charge.markSuccess(request.paymentKey(), response.getBody().method());
            return response.getBody();
        } else {
            charge.markFailed();
            throw new RuntimeException("결제 승인 실패");
        }
    }
}
