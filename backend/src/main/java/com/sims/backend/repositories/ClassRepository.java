package com.sims.backend.repositories;

import com.sims.backend.models.ClassModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;



public interface ClassRepository extends JpaRepository<ClassModel, Long> {
    List<ClassModel> findByClassNameContainingIgnoreCase(String className);
    boolean existsByClassName(String className);


    List<ClassModel> findByAcademicYear(String academicYear);
    List<ClassModel> findByDepartmentModel_departmentIdAndAcademicYear(Long departmentId, String academicYear);
    

    Optional<ClassModel> findByClassId(Long classId); 
    Optional<ClassModel> findByDepartmentModel_departmentId(Long departmentId);

    
}