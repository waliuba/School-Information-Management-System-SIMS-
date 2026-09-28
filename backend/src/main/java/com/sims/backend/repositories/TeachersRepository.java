package com.sims.backend.repositories;

import com.sims.backend.models.TeachersModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeachersRepository extends JpaRepository<TeachersModel, Long> {

    Optional<TeachersModel> findByTeacherNo(String teacherNo);

    Optional<TeachersModel> findByEmail(String email);

    boolean existsByTeacherNo(String teacherNo);

    boolean existsByEmail(String email);
}