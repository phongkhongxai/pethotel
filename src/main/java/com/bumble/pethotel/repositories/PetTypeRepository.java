package com.bumble.pethotel.repositories;

import com.bumble.pethotel.models.entity.PetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
    @Query("SELECT p FROM PetType p WHERE p.isDelete = false")
    List<PetType> findAllNotDeleted();


}
