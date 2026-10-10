package com.sims.backend.dtos.attendance;

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
public class AttendanceResponseDTO {

    private Long attendanceId;

    private Long studentId;

    private String studentName;

    private LocalDate attendanceDate;

    private String status;

    private String remarks;
}
