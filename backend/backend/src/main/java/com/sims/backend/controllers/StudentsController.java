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
import com.sims.backend.dtos.StudentsRequestDTO;
import com.sims.backend.dtos.StudentsResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.services.StudentsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentsController {

    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentsResponseDTO>>> getStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status) {
        List<StudentsResponseDTO> students = studentsService.getStudents(name, classId, className, departmentId, status);

        if (students.isEmpty()) {
            throw new ResourceNotFoundException("No students found");
        }

        return ResponseEntity.ok(ApiResponse.of("Students retrieved successfully", students));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentsResponseDTO>> getStudentById(@PathVariable Long studentId) {
        StudentsResponseDTO student = studentsService.getStudentById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return ResponseEntity.ok(ApiResponse.of("Student retrieved successfully", student));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentsResponseDTO>> createStudent(@Valid @RequestBody StudentsRequestDTO student) {
        StudentsResponseDTO createdStudent = studentsService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Student created successfully", createdStudent));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentsResponseDTO>> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentsRequestDTO student) {
        StudentsResponseDTO updatedStudent = studentsService.updateStudent(studentId, student);
        return ResponseEntity.ok(ApiResponse.of("Student updated successfully", updatedStudent));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long studentId) {
        studentsService.deleteStudentById(studentId);
        return ResponseEntity.ok(ApiResponse.of("Student deleted successfully", null));
    }

}
