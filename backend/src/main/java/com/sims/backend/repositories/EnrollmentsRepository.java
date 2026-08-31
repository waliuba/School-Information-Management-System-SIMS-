package com.sims.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.EnrollmentsModel;


public interface EnrollmentsRepository extends JpaRepository<EnrollmentsModel, Long> {
   
    List<EnrollmentsModel> findBySemester(Integer semester);

    List<EnrollmentsModel> findByStudentsModel_StudentId(Long studentId);

    List<EnrollmentsModel> findByStudentsModel_ClassModel_ClassId(Long classId);

    Optional<EnrollmentsModel> findByStudentsModel_StudentIdAndCourseModel_CourseId(
        Long studentId,
        Long courseId
    );

    

    List<EnrollmentsModel> findByDepartmentModel_DepartmentId(
        Long departmentId
    );

    List<EnrollmentsModel> findByStudentsModel_StudentIdAndSemester(
        Long studentId,
        Integer semester
    );

    List<EnrollmentsModel> findByCourseModel_CourseId(
        Long courseId
    );

    boolean existsByStudentsModel_StudentId(Long studentId);

    boolean existsByDepartmentModel_DepartmentId(Long departmentId);

    boolean existsByStudentsModel_ClassModel_ClassId(Long classId);

    boolean existsByCourseModel_CourseId(Long courseId);

    boolean existsByStudentsModel_StudentIdAndCourseModel_CourseId(Long studentId, Long courseId);

    boolean existsByStudentsModel_StudentIdAndCourseModel_CourseIdAndEnrollmentIdNot(
        Long studentId,
        Long courseId,
        Long enrollmentId
    );

    

}

    



    

