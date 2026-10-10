package com.sims.backend.mappers;

import com.sims.backend.dtos.attendance.AttendanceRequestDTO;
import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.models.AttendanceModel;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceModel toEntity(AttendanceRequestDTO dto) {
        return AttendanceModel.builder()
                .studentId(dto.getStudentId())
                .attendanceDate(dto.getAttendanceDate())
                .status(dto.getStatus())
                .remarks(dto.getRemarks())
                .build();
    }

    public AttendanceResponseDTO toResponseDTO(AttendanceModel entity, String studentName) {
        return AttendanceResponseDTO.builder()
                .attendanceId(entity.getAttendanceId())
                .studentId(entity.getStudentId())
                .studentName(studentName)
                .attendanceDate(entity.getAttendanceDate())
                .status(entity.getStatus())
                .remarks(entity.getRemarks())
                .build();
    }
}
