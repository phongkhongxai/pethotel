package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT p FROM Booking p WHERE p.isDelete = false")
    Page<Booking> findAllNotDeleted(Pageable pageable);
    Page<Booking> findByUserAndIsDeleteFalse(User user, Pageable pageable);
    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN b.room r " +
            "LEFT JOIN b.careServices cs " +
            "WHERE (r.shop.id = :shopId OR cs.shop.id = :shopId) AND b.isDelete = false")
    Page<Booking> findByShopIdAndIsDeleteFalse(@Param("shopId") Long shopId, Pageable pageable);


}
