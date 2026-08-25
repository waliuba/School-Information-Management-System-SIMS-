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
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dto.EnrollmentRequestDTO;
import com.sims.backend.dto.EnrollmentResponseDTO;
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
    public ResponseEntity<?> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getAllEnrollments();

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    public ResponseEntity<?> createEnrollment(@Valid @RequestBody EnrollmentRequestDTO enrollment) {
        if (enrollment == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Enrollment data is required"));
        }

        try {
            EnrollmentResponseDTO createdEnrollment = enrollmentsService.createEnrollment(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long enrollmentId) {
        if (enrollmentId == null || enrollmentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Enrollment id must be greater than zero"));
        }

        return enrollmentsService.getEnrollmentById(enrollmentId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Enrollment not found")));
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<?> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        if (studentId == null || studentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student id must be greater than zero"));
        }

        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByStudentId(studentId);

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found for the given student id"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/students/{studentId}/semester/{semester}")
    public ResponseEntity<?> getEnrollmentsByStudentIdAndSemester(
            @PathVariable Long studentId,
            @PathVariable Integer semester) {
        if (studentId == null || studentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student id must be greater than zero"));
        }

        if (semester == null || semester <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Semester must be a positive number"));
        }

        List<EnrollmentResponseDTO> enrollments =
                enrollmentsService.getEnrollmentsByStudentIdAndSemester(studentId, semester);

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found for the given student id and semester"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Course id must be greater than zero"));
        }

        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByCourseId(courseId);

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found for the given course id"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<?> getEnrollmentByStudentIdAndCourseId(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        if (studentId == null || studentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student id must be greater than zero"));
        }

        if (courseId == null || courseId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Course id must be greater than zero"));
        }

        return enrollmentsService.getEnrollmentByStudentIdAndCourseId(studentId, courseId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Enrollment not found for the given student id and course id")));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<?> getEnrollmentsBySemester(@PathVariable Integer semester) {
        if (semester == null || semester <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Semester must be a positive number"));
        }

        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsBySemester(semester);

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found for the given semester"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<?> getEnrollmentsByClassId(@PathVariable Long classId) {
        if (classId == null || classId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Class id must be greater than zero"));
        }

        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByClassId(classId);

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found for the given class id"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<?> getEnrollmentsByDepartmentId(@PathVariable Long departmentId) {
        if (departmentId == null || departmentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Department id must be greater than zero"));
        }

        List<EnrollmentResponseDTO> enrollments = enrollmentsService.getEnrollmentsByDepartmentId(departmentId);

        if (enrollments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No enrollments found for the given department id"));
        }

        return ResponseEntity.ok(enrollments);
    }

    @PutMapping("/{enrollmentId}")
    public ResponseEntity<?> updateEnrollment(
            @PathVariable Long enrollmentId,
            @Valid @RequestBody EnrollmentRequestDTO enrollment) {
        if (enrollmentId == null || enrollmentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Enrollment id must be greater than zero"));
        }

        try {
            EnrollmentResponseDTO updatedEnrollment =
                    enrollmentsService.updateEnrollment(enrollmentId, enrollment);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long enrollmentId) {
        if (enrollmentId == null || enrollmentId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Enrollment id must be greater than zero"));
        }

        boolean deleted = enrollmentsService.deleteEnrollmentById(enrollmentId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Enrollment not found"));
        }

        return ResponseEntity.noContent().build();
    }
}
