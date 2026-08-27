package com.sims.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EnrollmentRequestDTO {

    @NotNull(message = "Student id is required")
    @Positive(message = "Student id must be greater than zero")
    private Long studentId;

    @NotNull(message = "Department id is required")
    @Positive(message = "Department id must be greater than zero")
    private Long departmentId;

    @NotNull(message = "Course id is required")
    @Positive(message = "Course id must be greater than zero")
    private Long courseId;

    @NotNull(message = "Semester is required")
    @Positive(message = "Semester must be a positive number")
    private Integer semester;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
