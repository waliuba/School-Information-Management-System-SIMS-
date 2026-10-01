package com.sims.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sims.backend.models.CourseSubjectModel;

@Repository
public interface CourseSubjectRepository
        extends JpaRepository<CourseSubjectModel, Long> {

    List<CourseSubjectModel> findByCourseCourseId(Long courseId);

    List<CourseSubjectModel> findByUnitUnitId(Long unitId);

    List<CourseSubjectModel> findByStudentStudentId(Long studentId);

    List<CourseSubjectModel> findBySemester(Integer semester);

    List<CourseSubjectModel> findByYearOfStudy(Integer yearOfStudy);

    List<CourseSubjectModel> findBySubjectNameContainingIgnoreCase(
            String subjectName);

    boolean existsByCourseCourseId(Long courseId);
}