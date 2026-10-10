package com.sims.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sims.backend.dtos.subject.CourseSubjectRequestDTO;
import com.sims.backend.dtos.subject.CourseSubjectResponseDTO;
import com.sims.backend.services.CourseSubjectService;

@RestController
@RequestMapping("/api/course-subject")
public class CourseSubjectController {

    private final CourseSubjectService courseSubjectService;

    public CourseSubjectController(
            CourseSubjectService courseSubjectService) {

        this.courseSubjectService = courseSubjectService;
    }

    @GetMapping
    public ResponseEntity<List<CourseSubjectResponseDTO>> getAllCourseSubjects() {

        return ResponseEntity.ok(
                courseSubjectService.getAllCourseSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseSubjectResponseDTO> getCourseSubjectById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                courseSubjectService.getCourseSubjectById(id));
    }

    @PostMapping
    public ResponseEntity<CourseSubjectResponseDTO> createCourseSubject(
            @RequestBody CourseSubjectRequestDTO dto) {

        CourseSubjectResponseDTO created =
                courseSubjectService.createCourseSubject(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseSubjectResponseDTO> updateCourseSubject(
            @PathVariable Long id,
            @RequestBody CourseSubjectRequestDTO dto) {

        return ResponseEntity.ok(
                courseSubjectService.updateCourseSubject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseSubject(
            @PathVariable Long id) {

        courseSubjectService.deleteCourseSubject(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseSubjectResponseDTO>> getByCourse(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                courseSubjectService.getByCourse(courseId));
    }

    @GetMapping("/unit/{unitId}")
    public ResponseEntity<List<CourseSubjectResponseDTO>> getByUnit(
            @PathVariable Long unitId) {

        return ResponseEntity.ok(
                courseSubjectService.getByUnit(unitId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CourseSubjectResponseDTO>> getByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                courseSubjectService.getByStudent(studentId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseSubjectResponseDTO>> searchBySubjectName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                courseSubjectService.searchBySubjectName(name));
    }
}
