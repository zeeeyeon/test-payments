package jiyeon.app.payments.point.dto;

public record PaymentConfirmRequest(
    String orderId,
    String paymentKey,
    Long amount
) {}
