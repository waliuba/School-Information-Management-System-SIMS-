package com.sims.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.CourseSubjectDTO;
import com.sims.backend.mappers.CourseSubjectMapper;
import com.sims.backend.models.CourseSubjectModel;
import com.sims.backend.models.Courses;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.models.UnitsModel;
import com.sims.backend.repositories.CourseSubjectRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.repositories.UnitsRepository;

@Service
public class CourseSubjectService {

    private final CourseSubjectRepository courseSubjectRepository;
    private final CoursesRepository coursesRepository;
    private final UnitsRepository unitsRepository;
    private final StudentsRepository studentsRepository;

    public CourseSubjectService(
            CourseSubjectRepository courseSubjectRepository,
            CoursesRepository coursesRepository,
            UnitsRepository unitsRepository,
            StudentsRepository studentsRepository) {

        this.courseSubjectRepository = courseSubjectRepository;
        this.coursesRepository = coursesRepository;
        this.unitsRepository = unitsRepository;
        this.studentsRepository = studentsRepository;
    }

    public List<CourseSubjectDTO> getAllCourseSubjects() {

        return courseSubjectRepository.findAll()
                .stream()
                .map(CourseSubjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourseSubjectDTO getCourseSubjectById(Long id) {

        CourseSubjectModel model = courseSubjectRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course subject not found with ID: " + id));

        return CourseSubjectMapper.toDTO(model);
    }

    public CourseSubjectDTO createCourseSubject(
            CourseSubjectDTO dto) {

        Courses course = coursesRepository.findById(dto.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course not found with ID: "
                                        + dto.getCourseId()));

        UnitsModel unit = unitsRepository.findById(dto.getUnitId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unit not found with ID: "
                                        + dto.getUnitId()));

        StudentsModel student = studentsRepository.findById(dto.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with ID: "
                                        + dto.getStudentId()));

        CourseSubjectModel model =
                CourseSubjectMapper.toModel(
                        dto,
                        course,
                        unit,
                        student);

        CourseSubjectModel saved =
                courseSubjectRepository.save(model);

        return CourseSubjectMapper.toDTO(saved);
    }

    public CourseSubjectDTO updateCourseSubject(
            Long id,
            CourseSubjectDTO dto) {

        CourseSubjectModel existing =
                courseSubjectRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Course subject not found with ID: "
                                                + id));

        Courses course = coursesRepository.findById(dto.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course not found with ID: "
                                        + dto.getCourseId()));

        UnitsModel unit = unitsRepository.findById(dto.getUnitId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unit not found with ID: "
                                        + dto.getUnitId()));

        StudentsModel student =
                studentsRepository.findById(dto.getStudentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found with ID: "
                                                + dto.getStudentId()));

        existing.setCourse(course);
        existing.setUnit(unit);
        existing.setSubjectName(dto.getSubjectName());
        existing.setSemester(dto.getSemester());
        existing.setYearOfStudy(dto.getYearOfStudy());
        existing.setStudent(student);

        CourseSubjectModel updated =
                courseSubjectRepository.save(existing);

        return CourseSubjectMapper.toDTO(updated);
    }

    public void deleteCourseSubject(Long id) {

        if (!courseSubjectRepository.existsById(id)) {
            throw new RuntimeException(
                    "Course subject not found with ID: " + id);
        }

        courseSubjectRepository.deleteById(id);
    }

    public List<CourseSubjectDTO> getByCourse(Long courseId) {

        return courseSubjectRepository
                .findByCourseCourseId(courseId)
                .stream()
                .map(CourseSubjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseSubjectDTO> getByUnit(Long unitId) {

        return courseSubjectRepository
                .findByUnitUnitId(unitId)
                .stream()
                .map(CourseSubjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseSubjectDTO> getByStudent(Long studentId) {

        return courseSubjectRepository
                .findByStudentStudentId(studentId)
                .stream()
                .map(CourseSubjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseSubjectDTO> searchBySubjectName(
            String subjectName) {

        return courseSubjectRepository
                .findBySubjectNameContainingIgnoreCase(subjectName)
                .stream()
                .map(CourseSubjectMapper::toDTO)
                .collect(Collectors.toList());
    }
}