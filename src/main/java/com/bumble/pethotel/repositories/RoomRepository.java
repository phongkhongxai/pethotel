package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.CareService;
import com.bumble.pethotel.models.entity.Room;
import com.bumble.pethotel.models.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.isDelete = false")
    Page<Room> findAllNotDeleted(Pageable pageable);

    boolean existsByShopAndCode(Shop shop, String sign);
    Page<Room> findByShopAndIsDeleteFalse(Shop shop, Pageable pageable);
    long countByStatusAndSignContainingIgnoreCase(String status, String name);

}
