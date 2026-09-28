package com.sims.backend.repositories;

import com.sims.backend.models.EnrollmentsModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link EnrollmentsRepository}.
 */
@Generated
public class EnrollmentsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public EnrollmentsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#existsByCourseModel_CourseId(java.lang.Long)}.
   */
  public boolean existsByCourseModel_CourseId(Long courseId) {
    String queryString = "SELECT e.enrollmentId FROM EnrollmentsModel e WHERE e.courseModel.courseId = :courseId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseId", courseId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#existsByDepartmentModel_DepartmentId(java.lang.Long)}.
   */
  public boolean existsByDepartmentModel_DepartmentId(Long departmentId) {
    String queryString = "SELECT e.enrollmentId FROM EnrollmentsModel e WHERE e.departmentModel.departmentId = :departmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#existsByStudentsModel_ClassModel_ClassId(java.lang.Long)}.
   */
  public boolean existsByStudentsModel_ClassModel_ClassId(Long classId) {
    String queryString = "SELECT e.enrollmentId FROM EnrollmentsModel e LEFT JOIN e.studentsModel s LEFT JOIN s.classModel c WHERE c.classId = :classId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#existsByStudentsModel_StudentId(java.lang.Long)}.
   */
  public boolean existsByStudentsModel_StudentId(Long studentId) {
    String queryString = "SELECT e.enrollmentId FROM EnrollmentsModel e WHERE e.studentsModel.studentId = :studentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("studentId", studentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#existsByStudentsModel_StudentIdAndCourseModel_CourseId(java.lang.Long,java.lang.Long)}.
   */
  public boolean existsByStudentsModel_StudentIdAndCourseModel_CourseId(Long studentId,
      Long courseId) {
    String queryString = "SELECT e.enrollmentId FROM EnrollmentsModel e WHERE e.studentsModel.studentId = :studentId AND e.courseModel.courseId = :courseId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("studentId", studentId);
    query.setParameter("courseId", courseId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#existsByStudentsModel_StudentIdAndCourseModel_CourseIdAndEnrollmentIdNot(java.lang.Long,java.lang.Long,java.lang.Long)}.
   */
  public boolean existsByStudentsModel_StudentIdAndCourseModel_CourseIdAndEnrollmentIdNot(
      Long studentId, Long courseId, Long enrollmentId) {
    String queryString = "SELECT e.enrollmentId FROM EnrollmentsModel e WHERE e.studentsModel.studentId = :studentId AND e.courseModel.courseId = :courseId AND e.enrollmentId != :enrollmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("studentId", studentId);
    query.setParameter("courseId", courseId);
    query.setParameter("enrollmentId", enrollmentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findByCourseModel_CourseId(java.lang.Long)}.
   */
  public List<EnrollmentsModel> findByCourseModel_CourseId(Long courseId) {
    String queryString = "SELECT e FROM EnrollmentsModel e WHERE e.courseModel.courseId = :courseId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("courseId", courseId);

    return (List<EnrollmentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findByDepartmentModel_DepartmentId(java.lang.Long)}.
   */
  public List<EnrollmentsModel> findByDepartmentModel_DepartmentId(Long departmentId) {
    String queryString = "SELECT e FROM EnrollmentsModel e WHERE e.departmentModel.departmentId = :departmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);

    return (List<EnrollmentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findBySemester(java.lang.Integer)}.
   */
  public List<EnrollmentsModel> findBySemester(Integer semester) {
    String queryString = "SELECT e FROM EnrollmentsModel e WHERE e.semester = :semester";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("semester", semester);

    return (List<EnrollmentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findByStudentsModel_ClassModel_ClassId(java.lang.Long)}.
   */
  public List<EnrollmentsModel> findByStudentsModel_ClassModel_ClassId(Long classId) {
    String queryString = "SELECT e FROM EnrollmentsModel e LEFT JOIN e.studentsModel s LEFT JOIN s.classModel c WHERE c.classId = :classId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);

    return (List<EnrollmentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findByStudentsModel_StudentId(java.lang.Long)}.
   */
  public List<EnrollmentsModel> findByStudentsModel_StudentId(Long studentId) {
    String queryString = "SELECT e FROM EnrollmentsModel e WHERE e.studentsModel.studentId = :studentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("studentId", studentId);

    return (List<EnrollmentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findByStudentsModel_StudentIdAndCourseModel_CourseId(java.lang.Long,java.lang.Long)}.
   */
  public Optional<EnrollmentsModel> findByStudentsModel_StudentIdAndCourseModel_CourseId(
      Long studentId, Long courseId) {
    String queryString = "SELECT e FROM EnrollmentsModel e WHERE e.studentsModel.studentId = :studentId AND e.courseModel.courseId = :courseId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("studentId", studentId);
    query.setParameter("courseId", courseId);

    return Optional.ofNullable((EnrollmentsModel) convertOne(query.getSingleResultOrNull(), false, EnrollmentsModel.class));
  }

  /**
   * AOT generated implementation of {@link EnrollmentsRepository#findByStudentsModel_StudentIdAndSemester(java.lang.Long,java.lang.Integer)}.
   */
  public List<EnrollmentsModel> findByStudentsModel_StudentIdAndSemester(Long studentId,
      Integer semester) {
    String queryString = "SELECT e FROM EnrollmentsModel e WHERE e.studentsModel.studentId = :studentId AND e.semester = :semester";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("studentId", studentId);
    query.setParameter("semester", semester);

    return (List<EnrollmentsModel>) query.getResultList();
  }
}
