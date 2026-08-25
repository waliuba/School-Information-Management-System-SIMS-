package com.sims.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.CourseUnits;

public interface CourseUnitsRepository extends JpaRepository<CourseUnits, Long> {

    List<CourseUnits> findByCourseId_CourseId(Long courseId);

    List<CourseUnits> findByUnitId_UnitId(Long unitId);

    List<CourseUnits> findBySemester(String semester);

    List<CourseUnits> findByYearofstudy(String yearofstudy);

    List<CourseUnits> findByCourseId_CourseIdAndSemester(Long courseId, String semester);
}
