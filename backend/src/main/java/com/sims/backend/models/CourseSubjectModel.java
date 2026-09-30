package com.sims.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_subject")
public class CourseSubjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_subject_id")
    private Long courseSubjectId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitsModel unit;

    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentsModel student;

    public Long getCourseSubjectId() {
        return courseSubjectId;
    }

    public void setCourseSubjectId(Long courseSubjectId) {
        this.courseSubjectId = courseSubjectId;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public UnitsModel getUnit() {
        return unit;
    }

    public void setUnit(UnitsModel unit) {
        this.unit = unit;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public StudentsModel getStudent() {
        return student;
    }

    public void setStudent(StudentsModel student) {
        this.student = student;
    }
}