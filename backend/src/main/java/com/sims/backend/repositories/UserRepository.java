package com.sims.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.enums.Role;
import com.sims.backend.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByUsername(String username);
    List<UserModel> findByUsernameContainingIgnoreCase(String username);
    long countByRole(Role role);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

