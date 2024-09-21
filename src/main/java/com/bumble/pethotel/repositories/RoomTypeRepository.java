package com.bumble.pethotel.repositories;


import com.bumble.pethotel.models.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    @Query("SELECT r FROM RoomType r WHERE r.isDelete = false")
    List<RoomType> findAllNotDeleted();
}
