package com.sims.backend.mappers;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sims.backend.dtos.enrollment.EnrollmentRequestDTO;
import com.sims.backend.dtos.enrollment.EnrollmentResponseDTO;
import com.sims.backend.models.Courses;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.EnrollmentsModel;
import com.sims.backend.models.StudentsModel;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO toDTO(EnrollmentsModel enrollment) {
        EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
        dto.setEnrollmentId(enrollment.getEnrollmentId());
        if (enrollment.getStudentsModel() != null) {
            dto.setStudentName(studentName(enrollment.getStudentsModel()));
        }
        if (enrollment.getDepartmentModel() != null) {
            dto.setDepartmentName(enrollment.getDepartmentModel().getDepartmentName());
        }
        if (enrollment.getCourseModel() != null) {
            dto.setCourseId(enrollment.getCourseModel().getCourseId());
        }
        dto.setSemester(enrollment.getSemester());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        return dto;
    }

    private static String studentName(StudentsModel student) {
        return Stream.of(student.getFirstName(), student.getMiddleName(), student.getLastName())
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(" "));
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
