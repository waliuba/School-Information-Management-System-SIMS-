package com.sims.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.UserModel;

public interface RoleRepository extends JpaRepository<UserModel, Long> {

    List<UserModel> findByRole(com.sims.backend.enums.Role role);
}
