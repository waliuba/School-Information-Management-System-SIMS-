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

import com.sims.backend.dto.StudentRequestDTO;
import com.sims.backend.dto.StudentResponseDTO;
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
    public ResponseEntity<?> getStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status) {
        List<StudentResponseDTO> students = studentsService.getStudents(name, classId, className, departmentId, status);

        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No students found"));
        }

        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        if (studentId == null || studentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student id must be greater than zero"));
        }

        return studentsService.getStudentById(studentId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Student not found")));
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequestDTO student) {
        try {
            StudentResponseDTO createdStudent = studentsService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentRequestDTO student) {
        try {
            StudentResponseDTO updatedStudent = studentsService.updateStudent(studentId, student);

            if (updatedStudent == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Student not found"));
            }

            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        if (studentId == null || studentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student id must be greater than zero"));
        }

        boolean deleted = studentsService.deleteStudentById(studentId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Student not found"));
        }

        return ResponseEntity.noContent().build();
    }

}
