package jiyeon.app.payments.point.dto;

public record PointChargeRequest(Long memberId, Long amount, String orderId) {}

