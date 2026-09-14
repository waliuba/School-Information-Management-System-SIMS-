package com.sims.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ClassRequestDTO {

    @NotBlank(message = "Class name is required")
    @Size(max = 50, message = "Class name must be at most 50 characters")
    private String className;

    @NotBlank(message = "Academic year is required")
    @Size(max = 20, message = "Academic year must be at most 20 characters")
    private String academicYear;

    @NotNull(message = "Department id is required")
    @Positive(message = "Department id must be greater than zero")
    private Long departmentId;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
