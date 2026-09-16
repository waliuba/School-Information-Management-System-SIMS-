package com.sims.backend.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.StudentPerformanceModel;

public interface StudentPerformanceRepository extends JpaRepository<StudentPerformanceModel, Long> {

    Optional<StudentPerformanceModel> findByStudentModel_StudentId(Long studentId);

    List<StudentPerformanceModel> findAllByOrderByScoreDesc();
}
