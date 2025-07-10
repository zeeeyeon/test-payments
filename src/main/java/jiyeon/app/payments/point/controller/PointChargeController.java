package jiyeon.app.payments.point.controller;

import jiyeon.app.payments.point.dto.PointChargeRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmRequest;
import jiyeon.app.payments.point.dto.PaymentConfirmResult;
import jiyeon.app.payments.point.service.PointChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point-charge")
@RequiredArgsConstructor
public class PointChargeController {

    private final PointChargeService pointChargeService;

    @PostMapping("/create")
    public ResponseEntity<Long> createCharge(@RequestBody PointChargeRequest request) {
        Long chargeId = pointChargeService.createCharge(request.memberId(), request.amount(), request.orderId());
        return ResponseEntity.ok(chargeId);
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirmResult> confirmCharge(@RequestBody PaymentConfirmRequest request) {
        PaymentConfirmResult result = pointChargeService.confirmCharge(request);
        return ResponseEntity.ok(result);
    }
}
