package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.Pet;
import com.bumble.pethotel.models.entity.Shop;
import com.bumble.pethotel.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT s FROM Shop s WHERE s.isDelete = false")
    Page<Shop> findAllNotDeleted(Pageable pageable);

    @Query("SELECT s FROM Shop s WHERE s.isDelete = false AND s.shopVerified = true")
    Page<Shop> findAllNotDeletedAndVerified(Pageable pageable);

    @Query("SELECT s FROM Shop s WHERE s.isDelete = false AND s.shopVerified = false")
    Page<Shop> findAllNotDeletedAndNotVerified(Pageable pageable);

    Page<Shop> findByUserAndIsDeleteFalse(User user, Pageable pageable);
    Page<Shop> findByUserAndIsDeleteFalseAndShopVerifiedTrue(User user, Pageable pageable);
}
