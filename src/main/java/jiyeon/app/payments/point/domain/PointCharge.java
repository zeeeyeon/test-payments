package jiyeon.app.payments.point.domain;

import jakarta.persistence.*;
import jiyeon.app.payments.global.common.BaseTimeEntity;
import jiyeon.app.payments.memberProfile.domain.MemberProfile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "amount", "status"})
public class PointCharge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile member;

    @Column(nullable = false)
    @Comment("충전 요청 금액")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Comment("결제 상태 (PENDING, SUCCESS, FAILED)")
    private PaymentStatus status;

    @Column(length = 100)
    @Comment("토스 결제 키 (결제 승인용)")
    private String paymentKey;

    @Column(length = 100)
    @Comment("주문 ID")
    private String orderId;

    @Column(length = 100)
    @Comment("결제 수단 (카드, 가상계좌 등)")
    private String method;

    public static PointCharge createPending(MemberProfile member, Long amount, String orderId) {
        PointCharge charge = new PointCharge();
        charge.member = member;
        charge.amount = amount;
        charge.status = PaymentStatus.PENDING;
        charge.orderId = orderId;
        return charge;
    }

    public void markSuccess(String paymentKey, String method) {
        this.paymentKey = paymentKey;
        this.method = method;
        this.status = PaymentStatus.SUCCESS;
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
    }
}
