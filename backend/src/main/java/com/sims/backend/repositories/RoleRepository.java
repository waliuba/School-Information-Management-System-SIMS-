package com.sims.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.Role;
import java.util.List;

public interface  RoleRepository extends JpaRepository<Role, Long>{

    List<Role> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);

}



    

