package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.CareService;
import com.bumble.pethotel.models.entity.Room;
import com.bumble.pethotel.models.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    @Query(value = "SELECT * FROM rooms r WHERE r.sign = :sign AND r.status = :status AND r.is_delete = false ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Room> findRandomAvailableRoomBySignAndStatus(@Param("sign") String sign, @Param("status") String status);

}
