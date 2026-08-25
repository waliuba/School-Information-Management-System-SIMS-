package com.sims.backend.controllers;

import java.util.List;
import java.util.Map;

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

import com.sims.backend.dto.CourseUnitRequestDTO;
import com.sims.backend.dto.CourseUnitResponseDTO;
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
    public ResponseEntity<?> getCourseUnits(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long unitId,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String yearOfStudy) {
        List<CourseUnitResponseDTO> courseUnits = courseUnitsService.getCourseUnits(courseId, unitId, semester, yearOfStudy);

        if (courseUnits.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No course units found"));
        }

        return ResponseEntity.ok(courseUnits);
    }

    @GetMapping("/{courseUnitId}")
    public ResponseEntity<?> getCourseUnitById(@PathVariable Long courseUnitId) {
        return courseUnitsService.getCourseUnitById(courseUnitId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Course unit not found")));
    }

    @PostMapping
    public ResponseEntity<?> createCourseUnit(@Valid @RequestBody CourseUnitRequestDTO courseUnit) {
        try {
            CourseUnitResponseDTO createdCourseUnit = courseUnitsService.createCourseUnit(courseUnit);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourseUnit);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{courseUnitId}")
    public ResponseEntity<?> updateCourseUnit(
            @PathVariable Long courseUnitId,
            @Valid @RequestBody CourseUnitRequestDTO courseUnit) {
        try {
            CourseUnitResponseDTO updatedCourseUnit = courseUnitsService.updateCourseUnit(courseUnitId, courseUnit);

            if (updatedCourseUnit == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Course unit not found"));
            }

            return ResponseEntity.ok(updatedCourseUnit);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{courseUnitId}")
    public ResponseEntity<?> deleteCourseUnit(@PathVariable Long courseUnitId) {
        boolean deleted = courseUnitsService.deleteCourseUnitById(courseUnitId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Course unit not found"));
        }

        return ResponseEntity.noContent().build();
    }
}
