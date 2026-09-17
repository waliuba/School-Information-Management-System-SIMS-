package com.sims.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sims.backend.models.Courses;
import java.util.Optional;
import java.util.List;



@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {

   
    Optional<Courses> findByCourseCode(String courseCode);
    

    List<Courses> findByCourseNameContainingIgnoreCase(String courseName);
    List<Courses> findByCourseName(String coursename);
    List<Courses> findByCourseNameOrStatus(String courseName, String status);
    List<Courses> findByStatus(String status);
    boolean existsByCourseCode(String courseCode);
    boolean existsByCourseName(String courseName);
    

}
