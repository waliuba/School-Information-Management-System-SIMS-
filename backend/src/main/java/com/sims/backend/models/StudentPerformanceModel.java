package com.sims.backend.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_performance")
public class StudentPerformanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private StudentsModel studentModel;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses courseModel;

    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "grade", nullable = false, length = 2)
    private String grade;

    @Column(name = "performance", nullable = false, length = 30)
    private String performance;

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public StudentsModel getStudentModel() {
        return studentModel;
    }

    public void setStudentModel(StudentsModel studentModel) {
        this.studentModel = studentModel;
    }

    public Courses getCourseModel() {
        return courseModel;
    }

    public void setCourseModel(Courses courseModel) {
        this.courseModel = courseModel;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }
}
