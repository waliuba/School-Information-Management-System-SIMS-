package com.sims.backend.mappers;

import com.sims.backend.dtos.attendance.AttendanceRequestDTO;
import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.models.AttendanceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "attendanceId", ignore = true)
    @Mapping(target = "student", ignore = true)
    AttendanceModel toEntity(AttendanceRequestDTO dto);

    @Mapping(target = "studentId", source = "student.studentId")
    @Mapping(
        target = "admissionNo",
        source = "student.admissionNo"
    )
    @Mapping(
        target = "studentName",
        expression = "java(attendance.getStudent().getFirstName() + \" \" + attendance.getStudent().getLastName())"
    )
    AttendanceResponseDTO toResponseDTO(AttendanceModel attendance);
}