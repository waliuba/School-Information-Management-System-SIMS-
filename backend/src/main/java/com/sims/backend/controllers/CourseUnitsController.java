package com.sims.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.dtos.CourseUnitRequestDTO;
import com.sims.backend.dtos.CourseUnitResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.services.CourseUnitsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/course-units")
public class CourseUnitsController {

    private final CourseUnitsService courseUnitsService;

    public CourseUnitsController(CourseUnitsService courseUnitsService) {
        this.courseUnitsService = courseUnitsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseUnitResponseDTO>>> getCourseUnits(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long unitId,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String yearOfStudy) {
        List<CourseUnitResponseDTO> courseUnits = courseUnitsService.getCourseUnits(courseId, unitId, semester, yearOfStudy);

        if (courseUnits.isEmpty()) {
            throw new ResourceNotFoundException("No course units found");
        }

        return ResponseEntity.ok(ApiResponse.of("Course units retrieved successfully", courseUnits));
    }

    @GetMapping("/{courseUnitId}")
    public ResponseEntity<ApiResponse<CourseUnitResponseDTO>> getCourseUnitById(@PathVariable Long courseUnitId) {
        CourseUnitResponseDTO courseUnit = courseUnitsService.getCourseUnitById(courseUnitId)
                .orElseThrow(() -> new ResourceNotFoundException("Course unit not found"));
        return ResponseEntity.ok(ApiResponse.of("Course unit retrieved successfully", courseUnit));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseUnitResponseDTO>> createCourseUnit(@Valid @RequestBody CourseUnitRequestDTO courseUnit) {
        CourseUnitResponseDTO createdCourseUnit = courseUnitsService.createCourseUnit(courseUnit);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Course unit created successfully", createdCourseUnit));
    }

    @PutMapping("/{courseUnitId}")
    public ResponseEntity<ApiResponse<CourseUnitResponseDTO>> updateCourseUnit(
            @PathVariable Long courseUnitId,
            @Valid @RequestBody CourseUnitRequestDTO courseUnit) {
        CourseUnitResponseDTO updatedCourseUnit = courseUnitsService.updateCourseUnit(courseUnitId, courseUnit);

        if (updatedCourseUnit == null) {
            throw new ResourceNotFoundException("Course unit not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Course unit updated successfully", updatedCourseUnit));
    }

    @DeleteMapping("/{courseUnitId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourseUnit(@PathVariable Long courseUnitId) {
        boolean deleted = courseUnitsService.deleteCourseUnitById(courseUnitId);

        if (!deleted) {
            throw new ResourceNotFoundException("Course unit not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Course unit deleted successfully", null));
    }
}
