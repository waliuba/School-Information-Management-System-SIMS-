package com.sims.backend.dtos.attendance;

import com.sims.backend.models.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AttendanceRequestDTO(

        @NotNull(message = "Student ID is required")
        Long studentId,

        @NotNull(message = "Attendance date is required")
        LocalDate attendanceDate,

        @NotNull(message = "Attendance status is required")
        AttendanceStatus status,

        String remarks
) {
}