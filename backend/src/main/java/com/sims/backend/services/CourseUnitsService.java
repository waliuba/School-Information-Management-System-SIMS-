package com.sims.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.dto.CourseUnitRequestDTO;
import com.sims.backend.dto.CourseUnitResponseDTO;
import com.sims.backend.mappers.CourseUnitMapper;
import com.sims.backend.models.CourseUnits;
import com.sims.backend.models.Courses;
import com.sims.backend.models.UnitsModel;
import com.sims.backend.repositories.CourseUnitsRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.UnitsRepository;

@Service
public class CourseUnitsService {

    private final CourseUnitsRepository courseUnitsRepository;
    private final CoursesRepository coursesRepository;
    private final UnitsRepository unitsRepository;

    public CourseUnitsService(
            CourseUnitsRepository courseUnitsRepository,
            CoursesRepository coursesRepository,
            UnitsRepository unitsRepository) {
        this.courseUnitsRepository = courseUnitsRepository;
        this.coursesRepository = coursesRepository;
        this.unitsRepository = unitsRepository;
    }

    public List<CourseUnitResponseDTO> getCourseUnits(
            Long courseId,
            Long unitId,
            String semester,
            String yearOfStudy) {
        if (courseId != null && courseId > 0 && semester != null && !semester.isBlank()) {
            return toDTOList(courseUnitsRepository.findByCourseId_CourseIdAndSemester(courseId, semester.trim()));
        }

        if (courseId != null && courseId > 0) {
            return toDTOList(courseUnitsRepository.findByCourseId_CourseId(courseId));
        }

        if (unitId != null && unitId > 0) {
            return toDTOList(courseUnitsRepository.findByUnitId_UnitId(unitId));
        }

        if (semester != null && !semester.isBlank()) {
            return toDTOList(courseUnitsRepository.findBySemester(semester.trim()));
        }

        if (yearOfStudy != null && !yearOfStudy.isBlank()) {
            return toDTOList(courseUnitsRepository.findByYearofstudy(yearOfStudy.trim()));
        }

        return toDTOList(courseUnitsRepository.findAll());
    }

    public Optional<CourseUnitResponseDTO> getCourseUnitById(Long courseUnitId) {
        if (courseUnitId == null || courseUnitId <= 0) {
            return Optional.empty();
        }

        return courseUnitsRepository.findById(courseUnitId).map(CourseUnitMapper::toDTO);
    }

    public CourseUnitResponseDTO createCourseUnit(CourseUnitRequestDTO courseUnitRequest) {
        CourseUnits courseUnit = toEntity(courseUnitRequest);
        validateCourseUnit(courseUnit);
        normalizeCourseUnit(courseUnit);
        return CourseUnitMapper.toDTO(courseUnitsRepository.save(courseUnit));
    }

    public CourseUnitResponseDTO updateCourseUnit(Long courseUnitId, CourseUnitRequestDTO courseUnitRequest) {
        if (courseUnitId == null || courseUnitId <= 0) {
            throw new IllegalArgumentException("Course unit id must be greater than zero");
        }

        if (!courseUnitsRepository.existsById(courseUnitId)) {
            return null;
        }

        CourseUnits courseUnit = toEntity(courseUnitRequest);
        validateCourseUnit(courseUnit);
        normalizeCourseUnit(courseUnit);
        courseUnit.setCourseunitId(courseUnitId);
        return CourseUnitMapper.toDTO(courseUnitsRepository.save(courseUnit));
    }

    public boolean deleteCourseUnitById(Long courseUnitId) {
        if (courseUnitId == null || courseUnitId <= 0 || !courseUnitsRepository.existsById(courseUnitId)) {
            return false;
        }

        courseUnitsRepository.deleteById(courseUnitId);
        return true;
    }

    private CourseUnits toEntity(CourseUnitRequestDTO courseUnitRequest) {
        if (courseUnitRequest == null) {
            throw new IllegalArgumentException("Course unit data is required");
        }

        Courses course = coursesRepository.findById(courseUnitRequest.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        UnitsModel unit = unitsRepository.findById(courseUnitRequest.getUnitId())
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));

        return CourseUnitMapper.toEntity(courseUnitRequest, course, unit);
    }

    private List<CourseUnitResponseDTO> toDTOList(List<CourseUnits> courseUnits) {
        return courseUnits.stream()
                .map(CourseUnitMapper::toDTO)
                .toList();
    }

    private void validateCourseUnit(CourseUnits courseUnit) {
        if (courseUnit == null) {
            throw new IllegalArgumentException("Course unit data is required");
        }
        if (courseUnit.getCourseId() == null || courseUnit.getCourseId().getCourseId() == null) {
            throw new IllegalArgumentException("Course is required");
        }
        if (courseUnit.getUnitId() == null || courseUnit.getUnitId().getUnitId() == null) {
            throw new IllegalArgumentException("Unit is required");
        }
        if (courseUnit.getSemester() == null || courseUnit.getSemester().isBlank()) {
            throw new IllegalArgumentException("Semester is required");
        }
        if (courseUnit.getYearofstudy() == null || courseUnit.getYearofstudy().isBlank()) {
            throw new IllegalArgumentException("Year of study is required");
        }
    }

    private void normalizeCourseUnit(CourseUnits courseUnit) {
        courseUnit.setSemester(courseUnit.getSemester().trim());
        courseUnit.setYearofstudy(courseUnit.getYearofstudy().trim());
        if (courseUnit.getUnitDescription() != null) {
            courseUnit.setUnitDescription(courseUnit.getUnitDescription().trim());
        }
    }
}
