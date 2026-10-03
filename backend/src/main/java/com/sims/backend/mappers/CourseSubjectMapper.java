package com.sims.backend.mappers;

import com.sims.backend.dtos.subject.CourseSubjectRequestDTO;
import com.sims.backend.dtos.subject.CourseSubjectResponseDTO;
import com.sims.backend.models.CourseSubjectModel;
import com.sims.backend.models.Courses;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.models.UnitsModel;

public class CourseSubjectMapper {

    public static CourseSubjectResponseDTO toDTO(CourseSubjectModel model) {

        if (model == null) {
            return null;
        }

        CourseSubjectResponseDTO dto = new CourseSubjectResponseDTO();

        dto.setCourseSubjectId(model.getCourseSubjectId());

        if (model.getCourse() != null) {
            dto.setCourseId(model.getCourse().getCourseId());
        }

        if (model.getUnit() != null) {
            dto.setUnitId(model.getUnit().getUnitId());
        }

        dto.setSubjectName(model.getSubjectName());
        dto.setSemester(model.getSemester());
        dto.setYearOfStudy(model.getYearOfStudy());

        if (model.getStudent() != null) {
            dto.setStudentId(model.getStudent().getStudentId());
        }

        return dto;
    }

    public static CourseSubjectModel toModel(
            CourseSubjectRequestDTO dto,
            Courses course,
            UnitsModel unit,
            StudentsModel student) {

        if (dto == null) {
            return null;
        }

        CourseSubjectModel model = new CourseSubjectModel();

        model.setCourse(course);
        model.setUnit(unit);
        model.setSubjectName(dto.getSubjectName());
        model.setSemester(dto.getSemester());
        model.setYearOfStudy(dto.getYearOfStudy());
        model.setStudent(student);

        return model;
    }
}
