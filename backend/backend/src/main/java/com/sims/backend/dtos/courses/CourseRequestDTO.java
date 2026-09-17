package com.sims.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CourseRequestDTO {

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must be at most 100 characters")
    private String courseName;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must be at most 20 characters")
    private String courseCode;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Positive(message = "Duration years must be greater than zero")
    private Integer durationYears;

    @Size(max = 20, message = "Status must be at most 20 characters")
    private String status;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
