package jiyeon.app.payments.point.service;

import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import jiyeon.app.payments.memberProfile.repository.MemberProfileRepository;
import jiyeon.app.payments.point.domain.PointCharge;
import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;
import jiyeon.app.payments.point.repository.PointChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointChargeServiceImpl implements PointChargeService {

    private final MemberProfileRepository memberProfileRepository;
    private final PointChargeRepository pointChargeRepository;
    private final PaymentConfirmProcessor paymentConfirmProcessor;

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
    public PaymentConfirmResult confirmCharge(PaymentConfirmRequest request) {
        return paymentConfirmProcessor.process(request);
    }
}
