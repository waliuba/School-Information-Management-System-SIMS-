package com.sims.backend.dtos;

public class CourseUnitResponseDTO {

    private Long courseunitId;
    private Long courseId;
    private Long unitId;
    private String unitDescription;
    private String semester;
    private String yearofstudy;

    public Long getCourseunitId() {
        return courseunitId;
    }

    public void setCourseunitId(Long courseunitId) {
        this.courseunitId = courseunitId;
    }

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
