package com.sims.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sims.backend.models.UserModel;
import java.util.List;
import java.util.Optional;




public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByUsername(String username);
    List<UserModel> findByUsernameContainingIgnoreCase(String username);
    List<UserModel> findByStatus(String status);
    List<UserModel> findByRoleModel_RoleId(Long roleId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
}
