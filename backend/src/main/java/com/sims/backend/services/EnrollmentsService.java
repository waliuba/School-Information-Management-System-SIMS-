package com.sims.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.dto.EnrollmentRequestDTO;
import com.sims.backend.dto.EnrollmentResponseDTO;
import com.sims.backend.mappers.EnrollmentMapper;
import com.sims.backend.models.Courses;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.EnrollmentsModel;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentsRepository;

@Service
public class EnrollmentsService {
    private final EnrollmentsRepository enrollmentsRepository;
    private final StudentsRepository studentsRepository;
    private final DepartmentRepository departmentRepository;
    private final CoursesRepository coursesRepository;

    public EnrollmentsService(
            EnrollmentsRepository enrollmentsRepository,
            StudentsRepository studentsRepository,
            DepartmentRepository departmentRepository,
            CoursesRepository coursesRepository) {
        this.enrollmentsRepository = enrollmentsRepository;
        this.studentsRepository = studentsRepository;
        this.departmentRepository = departmentRepository;
        this.coursesRepository = coursesRepository;
    }

    public List<EnrollmentResponseDTO> getAllEnrollments() {
        return toDTOList(enrollmentsRepository.findAll());
    }

    public List<EnrollmentResponseDTO> saveEnrollment(EnrollmentRequestDTO enrollmentRequest) {
        EnrollmentsModel enrollment = toEntity(enrollmentRequest);
        validateEnrollment(enrollment);
        return List.of(EnrollmentMapper.toDTO(enrollmentsRepository.save(enrollment)));
    }

    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequest) {
        EnrollmentsModel enrollment = toEntity(enrollmentRequest);
        validateEnrollment(enrollment);
        return EnrollmentMapper.toDTO(enrollmentsRepository.save(enrollment));
    }

    public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentsRepository.existsById(enrollmentId)) {
            throw new RuntimeException("Enrollment not found");
        }

        enrollmentsRepository.deleteById(enrollmentId);
    }

    public boolean deleteEnrollmentById(Long enrollmentId) {
        if (enrollmentId == null || enrollmentId <= 0 || !enrollmentsRepository.existsById(enrollmentId)) {
            return false;
        }

        enrollmentsRepository.deleteById(enrollmentId);
        return true;
    }

    public List<EnrollmentResponseDTO> getEnrollmentsBySemester(Integer semester) {
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        }
        return toDTOList(enrollmentsRepository.findBySemester(semester));
    }

    public Optional<EnrollmentResponseDTO> getEnrollmentById(Long enrollmentId) {
        if (enrollmentId == null || enrollmentId <= 0) {
            return Optional.empty();
        }

        return enrollmentsRepository.findById(enrollmentId).map(EnrollmentMapper::toDTO);
    }

    public List<EnrollmentResponseDTO> getEnrollmentsByStudentId(Long studentId) {
        return toDTOList(enrollmentsRepository.findByStudentsModel_StudentId(studentId));
    }

    public List<EnrollmentResponseDTO> getEnrollmentsByClassId(Long classId) {
        return toDTOList(enrollmentsRepository.findByStudentsModel_ClassModel_ClassId(classId));
    }

    public List<EnrollmentResponseDTO> getEnrollmentsByStudentIdAndSemester(Long studentId, Integer semester) {
        return toDTOList(enrollmentsRepository.findByStudentsModel_StudentIdAndSemester(studentId, semester));
    }

    public Optional<EnrollmentResponseDTO> getEnrollmentByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentsRepository.findByStudentsModel_StudentIdAndCourseModel_CourseId(studentId, courseId)
                .map(EnrollmentMapper::toDTO);
    }

    public List<EnrollmentResponseDTO> getEnrollmentsByCourseId(Long courseId) {
        return toDTOList(enrollmentsRepository.findByCourseModel_CourseId(courseId));
    }

    public List<EnrollmentResponseDTO> getEnrollmentsByDepartmentId(Long departmentId) {
        return toDTOList(enrollmentsRepository.findByDepartmentModel_DepartmentId(departmentId));
    }

    public EnrollmentResponseDTO updateEnrollment(Long enrollmentId, EnrollmentRequestDTO enrollmentRequest) {
        EnrollmentsModel updatedEnrollment = toEntity(enrollmentRequest);
        validateEnrollment(updatedEnrollment);

        EnrollmentsModel existingEnrollment =
                enrollmentsRepository.findById(enrollmentId)
                        .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        existingEnrollment.setStudentsModel(updatedEnrollment.getStudentsModel());
        existingEnrollment.setDepartmentModel(updatedEnrollment.getDepartmentModel());
        existingEnrollment.setSemester(updatedEnrollment.getSemester());
        existingEnrollment.setCourseModel(updatedEnrollment.getCourseModel());
        existingEnrollment.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());

        return EnrollmentMapper.toDTO(enrollmentsRepository.save(existingEnrollment));
    }

    private EnrollmentsModel toEntity(EnrollmentRequestDTO enrollmentRequest) {
        if (enrollmentRequest == null) {
            throw new IllegalArgumentException("Enrollment data is required");
        }

        StudentsModel student = studentsRepository.findById(enrollmentRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        DepartmentModel department = departmentRepository.findById(enrollmentRequest.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        Courses course = coursesRepository.findById(enrollmentRequest.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return EnrollmentMapper.toEntity(enrollmentRequest, student, department, course);
    }

    private List<EnrollmentResponseDTO> toDTOList(List<EnrollmentsModel> enrollments) {
        return enrollments.stream()
                .map(EnrollmentMapper::toDTO)
                .toList();
    }

    private void validateEnrollment(EnrollmentsModel enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment data is required");
        }
        if (enrollment.getStudentsModel() == null || enrollment.getStudentsModel().getStudentId() == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (enrollment.getDepartmentModel() == null || enrollment.getDepartmentModel().getDepartmentId() == null) {
            throw new IllegalArgumentException("Department is required");
        }
        if (enrollment.getCourseModel() == null || enrollment.getCourseModel().getCourseId() == null) {
            throw new IllegalArgumentException("Course is required");
        }
        if (enrollment.getSemester() == null || enrollment.getSemester() <= 0) {
            throw new IllegalArgumentException("Semester must be a positive number");
        }
        if (enrollment.getEnrollmentDate() == null) {
            throw new IllegalArgumentException("Enrollment date is required");
        }
    }
}
