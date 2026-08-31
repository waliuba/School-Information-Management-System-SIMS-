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
import com.sims.backend.dtos.ClassRequestDTO;
import com.sims.backend.dtos.ClassResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
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
    public ResponseEntity<ApiResponse<List<ClassResponseDTO>>> getClasses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) Long departmentId) {
        List<ClassResponseDTO> classes = classService.getClasses(name, academicYear, departmentId);

        if (classes.isEmpty()) {
            throw new ResourceNotFoundException("No classes found");
        }

        return ResponseEntity.ok(ApiResponse.of("Classes retrieved successfully", classes));
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ApiResponse<ClassResponseDTO>> getClassById(@PathVariable Long classId) {
        ClassResponseDTO classModel = classService.getClassById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        return ResponseEntity.ok(ApiResponse.of("Class retrieved successfully", classModel));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponseDTO>> createClass(@Valid @RequestBody ClassRequestDTO classModel) {
        ClassResponseDTO createdClass = classService.createClass(classModel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Class created successfully", createdClass));
    }

    @PutMapping("/{classId}")
    public ResponseEntity<ApiResponse<ClassResponseDTO>> updateClass(
            @PathVariable Long classId,
            @Valid @RequestBody ClassRequestDTO classModel) {
        ClassResponseDTO updatedClass = classService.updateClass(classId, classModel);
        return ResponseEntity.ok(ApiResponse.of("Class updated successfully", updatedClass));
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(@PathVariable Long classId) {
        boolean deleted = classService.deleteClassById(classId);

        if (!deleted) {
            throw new ResourceNotFoundException("Class not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Class deleted successfully", null));
    }
}
