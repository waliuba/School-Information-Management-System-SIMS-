package com.sims.backend.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.AttendanceModel;

public interface AttendanceRepository extends JpaRepository<AttendanceModel, Long> {

    boolean existsByStudentsModel_StudentIdAndAttendanceDate(
            Long studentId, LocalDate attendanceDate);
}
