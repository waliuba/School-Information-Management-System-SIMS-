package com.sims.backend.repositories;

<<<<<<< HEAD
import java.util.List;
=======
import com.sims.backend.enums.Role;
>>>>>>> 80a32a198f23eb05a7d45e0c53900cbd6b72f469

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.UserModel;

public interface RoleRepository extends JpaRepository<UserModel, Long> {

    List<UserModel> findByRole(com.sims.backend.enums.Role role);
}
