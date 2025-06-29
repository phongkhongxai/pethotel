package com.bumble.pethotel.repositories;



import com.bumble.pethotel.models.entity.Role;
import com.bumble.pethotel.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.is_delete = false and u.user_id = :userId", nativeQuery = true)
    User findExistUserById(Long userId);
    @Query("SELECT us FROM User us WHERE us.isDelete = false")
    Page<User> findAllNotDeleted(Pageable pageable);

    Page<User> findByRoleAndIsDeleteFalse(Role role, Pageable pageable);

    Optional<User> findByUid(String uid);
    Optional<User> findByResetPasswordToken(String token);
    Optional<User> findByResetPasswordTokenAndEmail(String token, String email);
    List<User> findByIsPremiumTrue();




}
