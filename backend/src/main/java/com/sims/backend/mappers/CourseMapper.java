package com.sims.backend.mappers;

import com.sims.backend.dto.CourseRequestDTO;
import com.sims.backend.dto.CourseResponseDTO;
import com.sims.backend.models.Courses;

public class CourseMapper {

    public static CourseResponseDTO toDTO(Courses course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setCourseName(course.getCourseName());
        dto.setCourseCode(course.getCourseCode());
        dto.setDescription(course.getDescription());
        dto.setDurationYears(course.getDurationYears());
        dto.setStatus(course.getStatus());
        return dto;
    }

    public static Courses toEntity(CourseRequestDTO dto) {
        Courses course = new Courses();
        course.setCourseName(dto.getCourseName());
        course.setCourseCode(dto.getCourseCode());
        course.setDescription(dto.getDescription());
        course.setDurationYears(dto.getDurationYears());
        course.setStatus(dto.getStatus());
        return course;
    }
}
