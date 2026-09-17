package com.sims.backend.mappers;

import com.sims.backend.dtos.CourseUnitRequestDTO;
import com.sims.backend.dtos.CourseUnitResponseDTO;
import com.sims.backend.models.CourseUnits;
import com.sims.backend.models.Courses;
import com.sims.backend.models.UnitsModel;

public class CourseUnitMapper {

    public static CourseUnitResponseDTO toDTO(CourseUnits courseUnit) {
        CourseUnitResponseDTO dto = new CourseUnitResponseDTO();
        dto.setCourseunitId(courseUnit.getCourseunitId());
        if (courseUnit.getCourseId() != null) {
            dto.setCourseId(courseUnit.getCourseId().getCourseId());
        }
        if (courseUnit.getUnitId() != null) {
            dto.setUnitId(courseUnit.getUnitId().getUnitId());
        }
        dto.setUnitDescription(courseUnit.getUnitDescription());
        dto.setSemester(courseUnit.getSemester());
        dto.setYearofstudy(courseUnit.getYearofstudy());
        return dto;
    }

    public static CourseUnits toEntity(CourseUnitRequestDTO dto, Courses course, UnitsModel unit) {
        CourseUnits courseUnit = new CourseUnits();
        courseUnit.setCourseId(course);
        courseUnit.setUnitId(unit);
        courseUnit.setUnitDescription(dto.getUnitDescription());
        courseUnit.setSemester(dto.getSemester());
        courseUnit.setYearofstudy(dto.getYearofstudy());
        return courseUnit;
    }
}
