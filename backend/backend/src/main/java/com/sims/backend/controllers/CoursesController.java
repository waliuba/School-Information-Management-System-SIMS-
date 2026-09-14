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
import com.sims.backend.dtos.CourseRequestDTO;
import com.sims.backend.dtos.CourseResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.services.CoursesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CoursesController {

    private final CoursesService coursesService;

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getCourses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String courseCode) {
        List<CourseResponseDTO> courses = coursesService.getCourses(name, status, courseCode);

        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("No courses found");
        }

        return ResponseEntity.ok(ApiResponse.of("Courses retrieved successfully", courses));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(@PathVariable Long courseId) {
        CourseResponseDTO course = coursesService.getCourseById(courseId);

        if (course == null) {
            throw new ResourceNotFoundException("Course not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Course retrieved successfully", course));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(@Valid @RequestBody CourseRequestDTO course) {
        CourseResponseDTO createdCourse = coursesService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Course created successfully", createdCourse));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRequestDTO course) {
        CourseResponseDTO updatedCourse = coursesService.updateCourse(courseId, course);

        if (updatedCourse == null) {
            throw new ResourceNotFoundException("Course not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Course updated successfully", updatedCourse));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long courseId) {
        boolean deleted = coursesService.deleteCourseById(courseId);

        if (!deleted) {
            throw new ResourceNotFoundException("Course not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Course deleted successfully", null));
    }
}
