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
public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p WHERE p.isDelete = false")
    Page<Pet> findAllNotDeleted(Pageable pageable);

    Page<Pet> findByUserAndIsDeleteFalse(User user, Pageable pageable);
}
