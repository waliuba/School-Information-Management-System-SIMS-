package com.sims.backend.repositories;

import com.sims.backend.models.ClassModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link ClassRepository}.
 */
@Generated
public class ClassRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ClassRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ClassRepository#existsByClassName(java.lang.String)}.
   */
  public boolean existsByClassName(String className) {
    String queryString = "SELECT c.classId FROM ClassModel c WHERE c.className = :className";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("className", className);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link ClassRepository#existsByDepartmentModel_DepartmentId(java.lang.Long)}.
   */
  public boolean existsByDepartmentModel_DepartmentId(Long departmentId) {
    String queryString = "SELECT c.classId FROM ClassModel c WHERE c.departmentModel.departmentId = :departmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link ClassRepository#findByAcademicYear(java.lang.String)}.
   */
  public List<ClassModel> findByAcademicYear(String academicYear) {
    String queryString = "SELECT c FROM ClassModel c WHERE c.academicYear = :academicYear";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("academicYear", academicYear);

    return (List<ClassModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ClassRepository#findByClassId(java.lang.Long)}.
   */
  public Optional<ClassModel> findByClassId(Long classId) {
    String queryString = "SELECT c FROM ClassModel c WHERE c.classId = :classId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);

    return Optional.ofNullable((ClassModel) convertOne(query.getSingleResultOrNull(), false, ClassModel.class));
  }

  /**
   * AOT generated implementation of {@link ClassRepository#findByClassNameContainingIgnoreCase(java.lang.String)}.
   */
  public List<ClassModel> findByClassNameContainingIgnoreCase(String className) {
    String queryString = "SELECT c FROM ClassModel c WHERE UPPER(c.className) LIKE UPPER(:className) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("className", "%%%s%%".formatted(className != null ? className.toUpperCase() : className));

    return (List<ClassModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ClassRepository#findByDepartmentModel_departmentId(java.lang.Long)}.
   */
  public Optional<ClassModel> findByDepartmentModel_departmentId(Long departmentId) {
    String queryString = "SELECT c FROM ClassModel c WHERE c.departmentModel.departmentId = :departmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);

    return Optional.ofNullable((ClassModel) convertOne(query.getSingleResultOrNull(), false, ClassModel.class));
  }

  /**
   * AOT generated implementation of {@link ClassRepository#findByDepartmentModel_departmentIdAndAcademicYear(java.lang.Long,java.lang.String)}.
   */
  public List<ClassModel> findByDepartmentModel_departmentIdAndAcademicYear(Long departmentId,
      String academicYear) {
    String queryString = "SELECT c FROM ClassModel c WHERE c.departmentModel.departmentId = :departmentId AND c.academicYear = :academicYear";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);
    query.setParameter("academicYear", academicYear);

    return (List<ClassModel>) query.getResultList();
  }
}
