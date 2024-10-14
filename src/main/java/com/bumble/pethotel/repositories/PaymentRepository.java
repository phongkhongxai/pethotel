package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.Payment;
import com.bumble.pethotel.models.entity.Room;
import com.bumble.pethotel.models.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT r FROM Payment r WHERE r.isDelete = false")
    Page<Payment> findAllNotDeleted(Pageable pageable);

    // Tìm Payment theo orderCode
    @Query("SELECT p FROM Payment p WHERE p.orderCode = :orderCode AND p.isDelete = false")
    Optional<Payment> findByOrderCode(@Param("orderCode") Long orderCode);

}
