package com.sims.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CourseUnitRequestDTO {

    @NotNull(message = "Course id is required")
    @Positive(message = "Course id must be greater than zero")
    private Long courseId;

    @NotNull(message = "Unit id is required")
    @Positive(message = "Unit id must be greater than zero")
    private Long unitId;

    @Size(max = 500, message = "Unit description must be at most 500 characters")
    private String unitDescription;

    @NotBlank(message = "Semester is required")
    @Size(max = 20, message = "Semester must be at most 20 characters")
    private String semester;

    @NotBlank(message = "Year of study is required")
    @Size(max = 20, message = "Year of study must be at most 20 characters")
    private String yearofstudy;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYearofstudy() {
        return yearofstudy;
    }

    public void setYearofstudy(String yearofstudy) {
        this.yearofstudy = yearofstudy;
    }
}
