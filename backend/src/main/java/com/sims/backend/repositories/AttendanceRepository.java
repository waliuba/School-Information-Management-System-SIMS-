package com.sims.backend.repositories;

import com.sims.backend.models.AttendanceModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository
        extends JpaRepository<AttendanceModel, Long> {

    List<AttendanceModel> findByStudentId(Long studentId);

    List<AttendanceModel> findByAttendanceDate(LocalDate attendanceDate);

    List<AttendanceModel> findByStudentIdAndAttendanceDate(
            Long studentId,
            LocalDate attendanceDate
    );

    List<AttendanceModel> findByStatus(String status);
}