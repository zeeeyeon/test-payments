package jiyeon.app.payments.point.dto;

public record PaymentConfirmResult(String orderId, String paymentKey, String method, String status) {}
