package com.sims.backend.repositories;

import com.sims.backend.models.AttendanceModel;
import com.sims.backend.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceModel, Long> {

    Optional<AttendanceModel> findByStudentStudentIdAndAttendanceDate(
            Long studentId,
            LocalDate attendanceDate
    );

    List<AttendanceModel> findByStudentStudentId(Long studentId);

    List<AttendanceModel> findByAttendanceDate(LocalDate attendanceDate);

    List<AttendanceModel> findByStatus(AttendanceStatus status);

    List<AttendanceModel> findByStudentStudentIdAndStatus(
            Long studentId,
            AttendanceStatus status
    );

    boolean existsByStudentStudentIdAndAttendanceDate(
            Long studentId,
            LocalDate attendanceDate
    );
}