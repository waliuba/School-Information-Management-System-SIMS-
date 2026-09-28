package com.sims.backend.dtos.attendance;

import com.sims.backend.models.AttendanceStatus;

import java.time.LocalDate;

public record AttendanceResponseDTO(

        Long attendanceId,

        Long studentId,

        String admissionNo,

        String studentName,

        LocalDate attendanceDate,

        AttendanceStatus status,

        String remarks
) {
}