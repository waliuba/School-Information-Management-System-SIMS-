package com.sims.backend.repositories;

import com.sims.backend.models.DepartmentModel;
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
 * AOT generated JPA repository implementation for {@link DepartmentRepository}.
 */
@Generated
public class DepartmentRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public DepartmentRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link DepartmentRepository#existsByDepartmentName(java.lang.String)}.
   */
  public boolean existsByDepartmentName(String departmentName) {
    String queryString = "SELECT d.departmentId FROM DepartmentModel d WHERE d.departmentName = :departmentName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentName", departmentName);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link DepartmentRepository#findByDepartmentName(java.lang.String)}.
   */
  public Optional<DepartmentModel> findByDepartmentName(String departmentName) {
    String queryString = "SELECT d FROM DepartmentModel d WHERE d.departmentName = :departmentName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentName", departmentName);

    return Optional.ofNullable((DepartmentModel) convertOne(query.getSingleResultOrNull(), false, DepartmentModel.class));
  }

  /**
   * AOT generated implementation of {@link DepartmentRepository#findByDepartmentNameContainingIgnoreCase(java.lang.String)}.
   */
  public List<DepartmentModel> findByDepartmentNameContainingIgnoreCase(String departmentName) {
    String queryString = "SELECT d FROM DepartmentModel d WHERE UPPER(d.departmentName) LIKE UPPER(:departmentName) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("departmentName", "%%%s%%".formatted(departmentName != null ? departmentName.toUpperCase() : departmentName));

    return (List<DepartmentModel>) query.getResultList();
  }
}
