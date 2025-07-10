package jiyeon.app.payments.point.repository;

import jiyeon.app.payments.point.domain.PointCharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointChargeRepository extends JpaRepository<PointCharge, Long> {

    Optional<PointCharge> findByOrderId(String orderId);
}