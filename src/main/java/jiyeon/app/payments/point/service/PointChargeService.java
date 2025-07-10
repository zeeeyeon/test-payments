package jiyeon.app.payments.point.service;

import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;

public interface PointChargeService {

    Long createCharge(Long memberId, Long amount, String orderId);

    PaymentConfirmResult confirmCharge(PaymentConfirmRequest request);
}
