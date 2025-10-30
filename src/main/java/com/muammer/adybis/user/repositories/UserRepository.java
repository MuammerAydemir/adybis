package com.muammer.adybis.user.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.muammer.adybis.user.models.User;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u.id FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<UUID> findUsersIdByRoleName(@Param("roleName") String roleName);
}
