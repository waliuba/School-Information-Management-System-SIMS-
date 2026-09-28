package com.sims.backend.dtos.attendance;

import java.time.LocalDate;

import com.sims.backend.enums.AttendanceStatus;

public record AttendanceResponseDTO(
        Long attendanceId,
        Long studentId,
        String admissionNo,
        String studentName,
        LocalDate attendanceDate,
        AttendanceStatus status,
        String remarks) {
}
