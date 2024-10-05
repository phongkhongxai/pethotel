package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.CareService;
import com.bumble.pethotel.models.entity.Room;
import com.bumble.pethotel.models.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.isDelete = false")
    Page<Room> findAllNotDeleted(Pageable pageable);

    boolean existsByShopAndCode(Shop shop, String sign);
    Page<Room> findByShopAndIsDeleteFalse(Shop shop, Pageable pageable);
    long countByStatusAndSignContainingIgnoreCase(String status, String sign);

    @Query("SELECT DISTINCT r.sign FROM Room r WHERE r.shop.id = :shopId AND r.isDelete = false")
    List<String> findDistinctSignByShopId(Long shopId);
    // Phương thức để lấy các phòng còn trống theo sign và status
    @Query("SELECT r FROM Room r WHERE r.sign = ?1 AND r.status = ?2 AND r.isDelete = false")
    List<Room> findAvailableRoomsBySignAndStatus(String sign, String status);

    // Phương thức để đếm số phòng còn trống theo sign và status
    @Query("SELECT COUNT(r) FROM Room r WHERE r.sign = ?1 AND r.status = ?2 AND r.isDelete = false")
    long countAvailableRoomsBySignAndStatus(String sign, String status);

}
