package com.sims.backend.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "enrollments")    
public class EnrollmentsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentsModel studentsModel;


    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentModel departmentModel;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses courseModel;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    public Long getEnrollmentId() {
        return enrollmentId;
    }   
    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public StudentsModel getStudentsModel() {
        return studentsModel;
    }

    public void setStudentsModel(StudentsModel studentsModel) {
        this.studentsModel = studentsModel;
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

    public DepartmentModel getDepartmentModel() {
        return departmentModel;
    }
    public void setDepartmentModel(DepartmentModel departmentModel) {
        this.departmentModel = departmentModel;
    }

    public Courses getCourseModel() {
        return courseModel;
    }
    public void setCourseModel(Courses courseModel) {
        this.courseModel = courseModel;
    }
    
}
