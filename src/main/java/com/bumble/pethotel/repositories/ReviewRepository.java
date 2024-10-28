package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT p FROM Review p WHERE p.isDelete = false")
    Page<Review> findAllNotDeleted(Pageable pageable);
    Page<Review> findByShopAndIsDeleteFalse(Shop shop, Pageable pageable);
    Optional<Review> findByUserAndShopAndIsDeleteFalse(User user, Shop shop);

}
