package com.sims.backend.repositories;

import com.sims.backend.models.CourseUnits;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link CourseUnitsRepository}.
 */
@Generated
public class CourseUnitsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public CourseUnitsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#existsByCourseId_CourseId(java.lang.Long)}.
   */
  public boolean existsByCourseId_CourseId(Long courseId) {
    String queryString = "SELECT c.courseunitId FROM CourseUnits c WHERE c.courseId.courseId = :courseId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseId", courseId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#existsByUnitId_UnitId(java.lang.Long)}.
   */
  public boolean existsByUnitId_UnitId(Long unitId) {
    String queryString = "SELECT c.courseunitId FROM CourseUnits c WHERE c.unitId.unitId = :unitId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("unitId", unitId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#findByCourseId_CourseId(java.lang.Long)}.
   */
  public List<CourseUnits> findByCourseId_CourseId(Long courseId) {
    String queryString = "SELECT c FROM CourseUnits c WHERE c.courseId.courseId = :courseId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseId", courseId);

    return (List<CourseUnits>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#findByCourseId_CourseIdAndSemester(java.lang.Long,java.lang.String)}.
   */
  public List<CourseUnits> findByCourseId_CourseIdAndSemester(Long courseId, String semester) {
    String queryString = "SELECT c FROM CourseUnits c WHERE c.courseId.courseId = :courseId AND c.semester = :semester";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseId", courseId);
    query.setParameter("semester", semester);

    return (List<CourseUnits>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#findBySemester(java.lang.String)}.
   */
  public List<CourseUnits> findBySemester(String semester) {
    String queryString = "SELECT c FROM CourseUnits c WHERE c.semester = :semester";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("semester", semester);

    return (List<CourseUnits>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#findByUnitId_UnitId(java.lang.Long)}.
   */
  public List<CourseUnits> findByUnitId_UnitId(Long unitId) {
    String queryString = "SELECT c FROM CourseUnits c WHERE c.unitId.unitId = :unitId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("unitId", unitId);

    return (List<CourseUnits>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link CourseUnitsRepository#findByYearofstudy(java.lang.String)}.
   */
  public List<CourseUnits> findByYearofstudy(String yearofstudy) {
    String queryString = "SELECT c FROM CourseUnits c WHERE c.yearofstudy = :yearofstudy";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("yearofstudy", yearofstudy);

    return (List<CourseUnits>) query.getResultList();
  }
}
