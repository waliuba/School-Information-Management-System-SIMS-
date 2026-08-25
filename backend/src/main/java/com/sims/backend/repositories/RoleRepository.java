package com.sims.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.RoleModel;
import java.util.List;

public interface  RoleRepository extends JpaRepository<RoleModel, Long>{

    List<RoleModel> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);

}



    

