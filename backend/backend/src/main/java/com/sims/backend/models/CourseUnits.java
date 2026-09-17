package com.sims.backend.models;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;





@Entity
@Table(name = "course_units")
public class CourseUnits {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_unit_id")
    private Long courseunitId;


    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses courseId;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private UnitsModel unitId;

    @Column(name = "unit_description", length = 500)
    private String unitDescription;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "year_of_study", length = 20)
    private String yearofstudy;

    public Long getCourseunitId() {
        return courseunitId;
    }   
    public void setCourseunitId(Long courseunitId) {
        this.courseunitId = courseunitId;
    }
    public Courses getCourseId() {
        return courseId;
    }
    public void setCourseId(Courses courseId) {
        this.courseId = courseId;
    }
    public UnitsModel getUnitId() {
        return unitId;
    }   
    public void setUnitId(UnitsModel unitId) {
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
