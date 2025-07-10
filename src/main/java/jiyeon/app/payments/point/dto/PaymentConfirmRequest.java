package jiyeon.app.payments.point.dto;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Long amount
) {}
