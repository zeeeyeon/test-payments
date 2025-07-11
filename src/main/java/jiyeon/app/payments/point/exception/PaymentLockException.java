package jiyeon.app.payments.point.exception;

public class PaymentLockException extends RuntimeException {
    public PaymentLockException(String message) {
        super(message);
    }
}
