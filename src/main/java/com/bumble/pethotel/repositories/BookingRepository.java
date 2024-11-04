package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT p FROM Booking p WHERE p.isDelete = false")
    Page<Booking> findAllNotDeleted(Pageable pageable);
    Page<Booking> findByUserAndStatusAndIsDeleteFalse(User user, String status, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN b.room r " +
            "LEFT JOIN b.careServices cs " +
            "WHERE (r.shop.id = :shopId OR cs.shop.id = :shopId) AND b.isDelete = false AND b.status='COMPLETED'")
    Page<Booking> findByShopIdAndIsDeleteFalse(@Param("shopId") Long shopId, Pageable pageable);

    Optional<Booking> findByPayments_OrderCode(Long orderCode);
    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN b.room r " +
            "WHERE b.user = :user AND (r.shop.id = :shopId OR EXISTS " +
            "(SELECT cs FROM b.careServices cs WHERE cs.shop.id = :shopId)) " +
            "AND b.isDelete = false " +
            "ORDER BY b.dateBooking DESC")
    Optional<Booking> findLatestByUserAndShop(@Param("user") User user, @Param("shopId") Long shopId);
    List<Booking> findByEndDateBeforeAndStatus(LocalDate date, String status);
}
