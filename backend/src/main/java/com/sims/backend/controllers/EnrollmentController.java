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
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.dtos.EnrollmentRequestDTO;
import com.sims.backend.dtos.EnrollmentResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.services.EnrollmentsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentsService enrollmentsService;

    public EnrollmentController(EnrollmentsService enrollmentsService) {
        this.enrollmentsService = enrollmentsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getAllEnrollments();

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> createEnrollment(
            @Valid @RequestBody EnrollmentRequestDTO enrollment) {
        EnrollmentResponseDTO createdEnrollment = enrollmentsService.createEnrollment(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Enrollment created successfully", createdEnrollment));
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getEnrollmentById(@PathVariable Long enrollmentId) {
        EnrollmentResponseDTO enrollment = enrollmentsService.getEnrollmentById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return ResponseEntity.ok(ApiResponse.of("Enrollment retrieved successfully", enrollment));
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByStudentId(studentId);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found for the given student id");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/students/{studentId}/semester/{semester}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudentIdAndSemester(
            @PathVariable Long studentId,
            @PathVariable Integer semester) {
        List<EnrollmentResponseDTO> enrollments =
                enrollmentsService.getEnrollmentsByStudentIdAndSemester(studentId, semester);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found for the given student id and semester");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByCourseId(courseId);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found for the given course id");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getEnrollmentByStudentIdAndCourseId(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        EnrollmentResponseDTO enrollment = enrollmentsService.getEnrollmentByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for the given student id and course id"));
        return ResponseEntity.ok(ApiResponse.of("Enrollment retrieved successfully", enrollment));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsBySemester(@PathVariable Integer semester) {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsBySemester(semester);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found for the given semester");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByClassId(@PathVariable Long classId) {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByClassId(classId);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found for the given class id");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByDepartmentId(@PathVariable Long departmentId) {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByDepartmentId(departmentId);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("No enrollments found for the given department id");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollments retrieved successfully", enrollments));
    }

    @PutMapping("/{enrollmentId}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> updateEnrollment(
            @PathVariable Long enrollmentId,
            @Valid @RequestBody EnrollmentRequestDTO enrollment) {
        EnrollmentResponseDTO updatedEnrollment =
                enrollmentsService.updateEnrollment(enrollmentId, enrollment);
        return ResponseEntity.ok(ApiResponse.of("Enrollment updated successfully", updatedEnrollment));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long enrollmentId) {
        boolean deleted = enrollmentsService.deleteEnrollmentById(enrollmentId);

        if (!deleted) {
            throw new ResourceNotFoundException("Enrollment not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Enrollment deleted successfully", null));
    }
}
