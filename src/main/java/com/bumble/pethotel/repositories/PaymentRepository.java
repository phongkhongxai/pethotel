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
    @Query("SELECT r FROM Payment r WHERE r.isDelete = false AND r.method = 'PAYOS'")
    Page<Payment> findAllNotDeleted(Pageable pageable);
    @Query("SELECT r FROM Payment r WHERE r.isDelete = false AND r.method = 'PREMIUM'")
    Page<Payment> findAllPremiumNotDeleted(Pageable pageable);

    // Tìm Payment theo orderCode
    @Query("SELECT p FROM Payment p WHERE p.orderCode = :orderCode AND p.isDelete = false")
    Optional<Payment> findByOrderCode(@Param("orderCode") Long orderCode);


    @Query("""
        SELECT p FROM Payment p 
        LEFT JOIN p.booking.room r 
        LEFT JOIN p.booking.careServices cs
        WHERE p.isDelete = false 
          AND p.status = 'SUCCESS'
          AND (r.shop.id = :shopId OR cs.shop.id = :shopId)
        """)
    Page<Payment> findPaymentsByShop(@Param("shopId") Long shopId, Pageable pageable);



    // Thống kê tổng doanh thu của shop
    @Query("""
        SELECT SUM(p.amount) FROM Payment p
        LEFT JOIN p.booking.room r
        LEFT JOIN p.booking.careServices cs
        WHERE p.isDelete = false
          AND p.status = 'SUCCESS'
          AND p.method = 'PAYOS'
          AND (r.shop.id = :shopId OR cs.shop.id = :shopId)
        """)
    Double calculateTotalRevenueByShop(@Param("shopId") Long shopId);

    @Query("""
    SELECT SUM(p.amount * 0.1) FROM Payment p 
    LEFT JOIN p.booking.room r 
    LEFT JOIN p.booking.careServices cs
    WHERE p.isDelete = false 
      AND p.status = 'SUCCESS'
      AND p.method = 'PAYOS' 
      AND (r.shop.id = :shopId OR cs.shop.id = :shopId)
    """)
    Double calculateCommissionByShop(@Param("shopId") Long shopId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' AND p.isDelete = false AND p.method = 'PAYOS'")
    Double calculateTotalRevenueForSystem();

    @Query("SELECT SUM(p.amount * 0.1) FROM Payment p WHERE p.status = 'SUCCESS' AND p.isDelete = false AND p.method = 'PAYOS'")
    Double calculateTotalCommissionForSystem();
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' AND p.method = 'PREMIUM' AND p.isDelete = false ")
    Double calculatePremiumForSystem();

}
