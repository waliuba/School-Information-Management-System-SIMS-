package com.sims.backend.repositories;

import com.sims.backend.models.StudentsModel;
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
import org.springframework.data.repository.query.Param;

/**
 * AOT generated JPA repository implementation for {@link StudentsRepository}.
 */
@Generated
public class StudentsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public StudentsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#existsByAdmissionNo(java.lang.String)}.
   */
  public boolean existsByAdmissionNo(String admissionNo) {
    String queryString = "SELECT s.studentId FROM StudentsModel s WHERE s.admissionNo = :admissionNo";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("admissionNo", admissionNo);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#existsByAdmissionNoAndStudentIdNot(java.lang.String,java.lang.Long)}.
   */
  public boolean existsByAdmissionNoAndStudentIdNot(String admissionNo, Long studentId) {
    String queryString = "SELECT s.studentId FROM StudentsModel s WHERE s.admissionNo = :admissionNo AND s.studentId != :studentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("admissionNo", admissionNo);
    query.setParameter("studentId", studentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#existsByClassModel_ClassId(java.lang.Long)}.
   */
  public boolean existsByClassModel_ClassId(Long classId) {
    String queryString = "SELECT s.studentId FROM StudentsModel s WHERE s.classModel.classId = :classId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#existsByDepartmentModel_DepartmentId(java.lang.Long)}.
   */
  public boolean existsByDepartmentModel_DepartmentId(Long departmentId) {
    String queryString = "SELECT s.studentId FROM StudentsModel s WHERE s.departmentModel.departmentId = :departmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#existsByEmail(java.lang.String)}.
   */
  public boolean existsByEmail(String email) {
    String queryString = "SELECT s.studentId FROM StudentsModel s WHERE s.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#existsByEmailAndStudentIdNot(java.lang.String,java.lang.Long)}.
   */
  public boolean existsByEmailAndStudentIdNot(String email, Long studentId) {
    String queryString = "SELECT s.studentId FROM StudentsModel s WHERE s.email = :email AND s.studentId != :studentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);
    query.setParameter("studentId", studentId);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByAdmissionNo(java.lang.String)}.
   */
  public Optional<StudentsModel> findByAdmissionNo(String admissionNo) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.admissionNo = :admissionNo";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("admissionNo", admissionNo);

    return Optional.ofNullable((StudentsModel) convertOne(query.getSingleResultOrNull(), false, StudentsModel.class));
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByClassModel_ClassId(java.lang.Long)}.
   */
  public List<StudentsModel> findByClassModel_ClassId(Long classId) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.classModel.classId = :classId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByClassModel_ClassIdAndStatus(java.lang.Long,java.lang.String)}.
   */
  public List<StudentsModel> findByClassModel_ClassIdAndStatus(Long classId, String status) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.classModel.classId = :classId AND s.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);
    query.setParameter("status", status);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByClassModel_ClassIdAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(java.lang.Long,java.lang.String,java.lang.String,java.lang.String)}.
   */
  public List<StudentsModel> findByClassModel_ClassIdAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      Long classId, String status, String firstName, String lastName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.classModel.classId = :classId AND s.status = :status AND UPPER(s.firstName) LIKE UPPER(:firstName) ESCAPE '\\' OR UPPER(s.lastName) LIKE UPPER(:lastName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("classId", classId);
    query.setParameter("status", status);
    query.setParameter("firstName", "%%%s%%".formatted(firstName != null ? firstName.toUpperCase() : firstName));
    query.setParameter("lastName", "%%%s%%".formatted(lastName != null ? lastName.toUpperCase() : lastName));

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByClassModel_ClassName(java.lang.String)}.
   */
  public List<StudentsModel> findByClassModel_ClassName(String className) {
    String queryString = "SELECT s FROM StudentsModel s LEFT JOIN s.classModel c WHERE c.className = :className";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("className", className);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByClassModel_ClassNameAndStatus(java.lang.String,java.lang.String)}.
   */
  public List<StudentsModel> findByClassModel_ClassNameAndStatus(String className, String status) {
    String queryString = "SELECT s FROM StudentsModel s LEFT JOIN s.classModel c WHERE c.className = :className AND s.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("className", className);
    query.setParameter("status", status);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByClassModel_ClassNameAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(java.lang.String,java.lang.String,java.lang.String,java.lang.String)}.
   */
  public List<StudentsModel> findByClassModel_ClassNameAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String className, String status, String firstName, String lastName) {
    String queryString = "SELECT s FROM StudentsModel s LEFT JOIN s.classModel c WHERE c.className = :className AND s.status = :status AND UPPER(s.firstName) LIKE UPPER(:firstName) ESCAPE '\\' OR UPPER(s.lastName) LIKE UPPER(:lastName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("className", className);
    query.setParameter("status", status);
    query.setParameter("firstName", "%%%s%%".formatted(firstName != null ? firstName.toUpperCase() : firstName));
    query.setParameter("lastName", "%%%s%%".formatted(lastName != null ? lastName.toUpperCase() : lastName));

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByDepartmentModel_DepartmentId(java.lang.Long)}.
   */
  public List<StudentsModel> findByDepartmentModel_DepartmentId(Long departmentId) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.departmentModel.departmentId = :departmentId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentId", departmentId);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByEmail(java.lang.String)}.
   */
  public Optional<StudentsModel> findByEmail(String email) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);

    return Optional.ofNullable((StudentsModel) convertOne(query.getSingleResultOrNull(), false, StudentsModel.class));
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByFirstName(java.lang.String)}.
   */
  public List<StudentsModel> findByFirstName(String firstName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.firstName = :firstName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("firstName", firstName);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByFirstNameAndLastName(java.lang.String,java.lang.String)}.
   */
  public List<StudentsModel> findByFirstNameAndLastName(String firstName, String lastName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.firstName = :firstName AND s.lastName = :lastName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("firstName", firstName);
    query.setParameter("lastName", lastName);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByFirstNameContainingIgnoreCase(java.lang.String)}.
   */
  public List<StudentsModel> findByFirstNameContainingIgnoreCase(String firstName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE UPPER(s.firstName) LIKE UPPER(:firstName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("firstName", "%%%s%%".formatted(firstName != null ? firstName.toUpperCase() : firstName));

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(java.lang.String,java.lang.String)}.
   */
  public List<StudentsModel> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String firstName, String lastName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE UPPER(s.firstName) LIKE UPPER(:firstName) ESCAPE '\\' OR UPPER(s.lastName) LIKE UPPER(:lastName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("firstName", "%%%s%%".formatted(firstName != null ? firstName.toUpperCase() : firstName));
    query.setParameter("lastName", "%%%s%%".formatted(lastName != null ? lastName.toUpperCase() : lastName));

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByGender(java.lang.String)}.
   */
  public List<StudentsModel> findByGender(String gender) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.gender = :gender";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("gender", gender);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByLastName(java.lang.String)}.
   */
  public List<StudentsModel> findByLastName(String lastName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.lastName = :lastName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("lastName", lastName);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByLastNameContainingIgnoreCase(java.lang.String)}.
   */
  public List<StudentsModel> findByLastNameContainingIgnoreCase(String lastName) {
    String queryString = "SELECT s FROM StudentsModel s WHERE UPPER(s.lastName) LIKE UPPER(:lastName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("lastName", "%%%s%%".formatted(lastName != null ? lastName.toUpperCase() : lastName));

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#findByStatus(java.lang.String)}.
   */
  public List<StudentsModel> findByStatus(String status) {
    String queryString = "SELECT s FROM StudentsModel s WHERE s.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("status", status);

    return (List<StudentsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link StudentsRepository#searchStudents(java.lang.String,java.lang.Long,java.lang.String,java.lang.Long,java.lang.String)}.
   */
  public List<StudentsModel> searchStudents(@Param("name") String name,
      @Param("classId") Long classId, @Param("className") String className,
      @Param("departmentId") Long departmentId, @Param("status") String status) {
    String queryString = "select s from StudentsModel s\n"
            + "where (:name is null or :name = '' or\n"
            + "    lower(s.firstName) like lower(concat('%', :name, '%')) or\n"
            + "    lower(s.lastName) like lower(concat('%', :name, '%')) or\n"
            + "    lower(s.admissionNo) like lower(concat('%', :name, '%')))\n"
            + "and (:classId is null or s.classModel.classId = :classId)\n"
            + "and (:className is null or :className = '' or lower(s.classModel.className) = lower(:className))\n"
            + "and (:departmentId is null or s.departmentModel.departmentId = :departmentId)\n"
            + "and (:status is null or :status = '' or lower(s.status) = lower(:status))\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("name", name);
    query.setParameter("classId", classId);
    query.setParameter("className", className);
    query.setParameter("departmentId", departmentId);
    query.setParameter("status", status);

    return (List<StudentsModel>) query.getResultList();
  }
}
