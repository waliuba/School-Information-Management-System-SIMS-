package com.sims.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.DepartmentModel;

public interface DepartmentRepository extends JpaRepository<DepartmentModel, Long> {
    Optional<DepartmentModel> findByDepartmentName(String departmentName);
   
    List<DepartmentModel> findByDepartmentNameContainingIgnoreCase(String departmentName);
    boolean existsByDepartmentName(String departmentName);
}
