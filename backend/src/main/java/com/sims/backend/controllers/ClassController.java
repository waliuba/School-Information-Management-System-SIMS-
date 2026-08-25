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

import com.sims.backend.dto.ClassRequestDTO;
import com.sims.backend.dto.ClassResponseDTO;
import com.sims.backend.services.ClassService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<?> getClasses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) Long departmentId) {
        List<ClassResponseDTO> classes = classService.getClasses(name, academicYear, departmentId);

        if (classes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No classes found"));
        }

        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<?> getClassById(@PathVariable Long classId) {
        if (classId == null || classId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Class id must be greater than zero"));
        }

        return classService.getClassById(classId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Class not found")));
    }

    @PostMapping
    public ResponseEntity<?> createClass(@Valid @RequestBody ClassRequestDTO classModel) {
        if (classModel == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Class data is required"));
        }

        try {
            ClassResponseDTO createdClass = classService.createClass(classModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClass);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{classId}")
    public ResponseEntity<?> updateClass(
            @PathVariable Long classId,
            @Valid @RequestBody ClassRequestDTO classModel) {
        if (classId == null || classId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Class id must be greater than zero"));
        }

        try {
            ClassResponseDTO updatedClass = classService.updateClass(classId, classModel);
            return ResponseEntity.ok(updatedClass);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<?> deleteClass(@PathVariable Long classId) {
        if (classId == null || classId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Class id must be greater than zero"));
        }

        boolean deleted = classService.deleteClassById(classId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Class not found"));
        }

        return ResponseEntity.noContent().build();
    }
}
