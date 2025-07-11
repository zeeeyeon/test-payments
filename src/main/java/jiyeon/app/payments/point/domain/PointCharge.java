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

    @Column(nullable = false)
    private Long amount;

    private String paymentKey;

    private String orderId;

    private String method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile member;

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

    public boolean isAlreadyApproved() {
        return this.status == PaymentStatus.SUCCESS;
    }
}
