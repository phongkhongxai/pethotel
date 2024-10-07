package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.CareService;
import com.bumble.pethotel.models.entity.Pet;
import com.bumble.pethotel.models.entity.Shop;
import com.bumble.pethotel.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CareServiceRepository extends JpaRepository<CareService, Long> {
    @Query("SELECT s FROM CareService s WHERE s.isDelete = false")
    Page<CareService> findAllNotDeleted(Pageable pageable);

    Page<CareService> findByShopAndIsDeleteFalse(Shop shop, Pageable pageable);

    Page<CareService> findByShopAndTypeAndIsDeleteFalse(Shop shop, String type, Pageable pageable);
}
