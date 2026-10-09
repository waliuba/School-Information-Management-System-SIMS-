package com.sims.backend.dtos.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequestDTO {

    @NotNull(message = "Student is required")
    private Long studentId;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    @NotBlank(message = "Attendance status is required")
    @Pattern(regexp = "(?i)^(PRESENT|ABSENT|LATE)$", message = "Attendance status must be Present, Absent, or Late")
    @Size(max = 20, message = "Attendance status cannot exceed 20 characters")
    private String status;

    @Size(max = 255, message = "Remarks cannot exceed 255 characters")
    private String remarks;
}
