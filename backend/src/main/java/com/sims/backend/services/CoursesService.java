package com.sims.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.courses.CourseRequestDTO;
import com.sims.backend.dtos.courses.CourseResponseDTO;
import com.sims.backend.enums.Status;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.mappers.CourseMapper;
import com.sims.backend.models.Courses;
import com.sims.backend.repositories.CourseSubjectRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.exceptions.ResourceNotFoundException;

@Service
public class CoursesService {

    private final CoursesRepository coursesRepository;
    private final EnrollmentsRepository enrollmentsRepository;
    private final CourseSubjectRepository courseUnitsRepository;
    private final DepartmentRepository departmentRepository;

    public CoursesService(
            CoursesRepository coursesRepository,
            EnrollmentsRepository enrollmentsRepository,
            CourseSubjectRepository courseUnitsRepository,
            DepartmentRepository departmentRepository) {
        this.coursesRepository = coursesRepository;
        this.enrollmentsRepository = enrollmentsRepository;
        this.courseUnitsRepository = courseUnitsRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<CourseResponseDTO> getAllCourses() {
        return toDTOList(coursesRepository.findAll());
    }

    public CourseResponseDTO getCourseById(Long courseId) {
        if (courseId == null || courseId <= 0) {
            return null;
        }

        return coursesRepository.findById(courseId)
                .map(CourseMapper::toDTO)
                .orElse(null);
    }

    public List<CourseResponseDTO> getCourses(String name, String status, String courseCode) {
        if (courseCode != null && !courseCode.isBlank()) {
            return getCoursesByCourseCode(courseCode);
        }

        if (name != null && !name.isBlank() && status != null && !status.isBlank()) {
            return searchCoursesByNameOrStatus(name, status);
        }

        if (name != null && !name.isBlank()) {
            return toDTOList(coursesRepository.findByCourseNameContainingIgnoreCase(name.trim()));
        }

        if (status != null && !status.isBlank()) {
            return toDTOList(coursesRepository.findByStatus(parseStatus(status)));
        }

        return getAllCourses();
    }

    public CourseResponseDTO saveCourse(CourseRequestDTO courseRequest) {
        Courses course = toEntity(courseRequest);
        validateCourse(course);
        return CourseMapper.toDTO(coursesRepository.save(course));
    }

    public CourseResponseDTO createCourse(CourseRequestDTO courseRequest) {
        Courses course = toEntity(courseRequest);
        validateCourse(course);

        if (coursesRepository.existsByCourseCode(course.getCourseCode().trim())) {
            throw new BusinessRuleException("Course code already exists");
        }

        if (coursesRepository.existsByCourseName(course.getCourseName().trim())) {
            throw new BusinessRuleException("Course name already exists");
        }

        normalizeCourse(course);
        return CourseMapper.toDTO(coursesRepository.save(course));
    }

    public void deleteCourse(Long courseId) {
        if (courseId != null && courseId > 0) {
            coursesRepository.deleteById(courseId);
        }
    }

    public boolean deleteCourseById(Long courseId) {
        if (courseId == null || courseId <= 0 || !coursesRepository.existsById(courseId)) {
            return false;
        }

        if (enrollmentsRepository.existsByCourseModel_CourseId(courseId)
                || courseUnitsRepository.existsByCourseCourseId(courseId)) {
            throw new BusinessRuleException("Cannot delete course with existing enrollments or course units");
        }

        coursesRepository.deleteById(courseId);
        return true;
    }

    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO courseRequest) {
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course id must be greater than zero");
        }

        if (!coursesRepository.existsById(courseId)) {
            return null;
        }

        Courses course = toEntity(courseRequest);
        validateCourse(course);
        normalizeCourse(course);
        course.setCourseId(courseId);
        return CourseMapper.toDTO(coursesRepository.save(course));
    }

    public List<CourseResponseDTO> getCoursesByCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            return List.of();
        }
        return coursesRepository.findByCourseCode(courseCode.trim())
                .map(course -> List.of(CourseMapper.toDTO(course)))
                .orElse(List.of());
    }

    public List<CourseResponseDTO> getCoursesByCourseName(String courseName) {
        if (courseName == null || courseName.isBlank()) {
            return List.of();
        }
        return toDTOList(coursesRepository.findByCourseName(courseName.trim()));
    }

    public List<CourseResponseDTO> searchCoursesByNameOrStatus(String name, String status) {
        if ((name == null || name.isBlank()) && (status == null || status.isBlank())) {
            return getAllCourses();
        }

        String searchTerm = (name != null && !name.isBlank()) ? name.trim() : "";
        Status searchStatus = (status != null && !status.isBlank()) ? parseStatus(status) : null;

        return toDTOList(coursesRepository.findByCourseNameOrStatus(searchTerm, searchStatus));
    }

    private List<CourseResponseDTO> toDTOList(List<Courses> courses) {
        return courses.stream()
                .map(CourseMapper::toDTO)
                .toList();
    }

    private void validateCourse(Courses course) {
        if (course == null) {
            throw new IllegalArgumentException("Course data is required");
        }
        if (course.getCourseName() == null || course.getCourseName().isBlank()) {
            throw new IllegalArgumentException("Course name is required");
        }
        if (course.getCourseCode() == null || course.getCourseCode().isBlank()) {
            throw new IllegalArgumentException("Course code is required");
        }
        if (course.getDepartmentModel() == null) throw new IllegalArgumentException("Department is required");
        if (course.getDurationYears() != null && course.getDurationYears() <= 0) {
            throw new IllegalArgumentException("Duration years must be greater than zero");
        }
    }

    private Courses toEntity(CourseRequestDTO request) {
        Courses course = CourseMapper.toEntity(request);
        DepartmentModel department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        course.setDepartmentModel(department);
        return course;
    }

    private void normalizeCourse(Courses course) {
        course.setCourseName(course.getCourseName().trim());
        course.setCourseCode(course.getCourseCode().trim());
        if (course.getDescription() != null) {
            course.setDescription(course.getDescription().trim());
        }
    }

    private Status parseStatus(String status) {
        try {
            return Status.valueOf(status.trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Status must be Active or Inactive", exception);
        }
    }
}
