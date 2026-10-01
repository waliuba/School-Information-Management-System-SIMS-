package com.sims.backend.repositories;

import com.sims.backend.models.Courses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link CoursesRepository}.
 */
@Generated
public class CoursesRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public CoursesRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#existsByCourseCode(java.lang.String)}.
   */
  public boolean existsByCourseCode(String courseCode) {
    String queryString = "SELECT c.courseId FROM Courses c WHERE c.courseCode = :courseCode";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseCode", courseCode);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#existsByCourseName(java.lang.String)}.
   */
  public boolean existsByCourseName(String courseName) {
    String queryString = "SELECT c.courseId FROM Courses c WHERE c.courseName = :courseName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseName", courseName);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#findByCourseCode(java.lang.String)}.
   */
  public Optional<Courses> findByCourseCode(String courseCode) {
    String queryString = "SELECT c FROM Courses c WHERE c.courseCode = :courseCode";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseCode", courseCode);

    return Optional.ofNullable((Courses) convertOne(query.getSingleResultOrNull(), false, Courses.class));
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#findByCourseName(java.lang.String)}.
   */
  public List<Courses> findByCourseName(String coursename) {
    String queryString = "SELECT c FROM Courses c WHERE c.courseName = :coursename";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("coursename", coursename);

    return (List<Courses>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#findByCourseNameContainingIgnoreCase(java.lang.String)}.
   */
  public List<Courses> findByCourseNameContainingIgnoreCase(String courseName) {
    String queryString = "SELECT c FROM Courses c WHERE UPPER(c.courseName) LIKE UPPER(:courseName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseName", "%%%s%%".formatted(courseName != null ? courseName.toUpperCase() : courseName));

    return (List<Courses>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#findByCourseNameOrStatus(java.lang.String,java.lang.String)}.
   */
  public List<Courses> findByCourseNameOrStatus(String courseName, String status) {
    String queryString = "SELECT c FROM Courses c WHERE c.courseName = :courseName OR c.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseName", courseName);
    query.setParameter("status", status);

    return (List<Courses>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CoursesRepository#findByStatus(java.lang.String)}.
   */
  public List<Courses> findByStatus(String status) {
    String queryString = "SELECT c FROM Courses c WHERE c.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("status", status);

    return (List<Courses>) query.getResultList();
  }
}
