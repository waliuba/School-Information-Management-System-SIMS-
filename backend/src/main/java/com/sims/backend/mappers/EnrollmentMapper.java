package com.sims.backend.mappers;

import com.sims.backend.dtos.EnrollmentRequestDTO;
import com.sims.backend.dtos.EnrollmentResponseDTO;
import com.sims.backend.models.Courses;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.EnrollmentsModel;
import com.sims.backend.models.StudentsModel;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO toDTO(EnrollmentsModel enrollment) {
        EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
        dto.setEnrollmentId(enrollment.getEnrollmentId());
        if (enrollment.getStudentsModel() != null) {
            dto.setStudentId(enrollment.getStudentsModel().getStudentId());
        }
        if (enrollment.getDepartmentModel() != null) {
            dto.setDepartmentId(enrollment.getDepartmentModel().getDepartmentId());
        }
        if (enrollment.getCourseModel() != null) {
            dto.setCourseId(enrollment.getCourseModel().getCourseId());
        }
        dto.setSemester(enrollment.getSemester());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        return dto;
    }

    public static EnrollmentsModel toEntity(
            EnrollmentRequestDTO dto,
            StudentsModel student,
            DepartmentModel department,
            Courses course) {
        EnrollmentsModel enrollment = new EnrollmentsModel();
        enrollment.setStudentsModel(student);
        enrollment.setDepartmentModel(department);
        enrollment.setCourseModel(course);
        enrollment.setSemester(dto.getSemester());
        enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        return enrollment;
    }
}
