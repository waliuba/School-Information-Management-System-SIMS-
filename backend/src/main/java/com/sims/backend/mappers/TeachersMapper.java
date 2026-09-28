package com.sims.backend.mappers;

import com.sims.backend.dtos.teachers.*;

import com.sims.backend.models.TeachersModel;
import org.springframework.stereotype.Component;

@Component
public class TeachersMapper {

    public TeachersModel toEntity(TeacherRequestDTO dto) {

        return TeachersModel.builder()
                .teacherNo(dto.getTeacherNo())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }

    public TeacherResponseDTO toResponseDTO(TeachersModel teacher) {

        return TeacherResponseDTO.builder()
                .teacherId(teacher.getTeacherId())
                .teacherNo(teacher.getTeacherNo())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .build();
    }
}