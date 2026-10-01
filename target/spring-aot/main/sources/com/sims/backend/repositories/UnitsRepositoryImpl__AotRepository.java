package com.sims.backend.repositories;

import com.sims.backend.models.UnitsModel;
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
 * AOT generated JPA repository implementation for {@link UnitsRepository}.
 */
@Generated
public class UnitsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public UnitsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link UnitsRepository#findByStatus(java.lang.String)}.
   */
  public List<UnitsModel> findByStatus(String status) {
    String queryString = "SELECT u FROM UnitsModel u WHERE u.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("status", status);

    return (List<UnitsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link UnitsRepository#findByUnitCode(java.lang.String)}.
   */
  public Optional<UnitsModel> findByUnitCode(String unitCode) {
    String queryString = "SELECT u FROM UnitsModel u WHERE u.unitCode = :unitCode";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("unitCode", unitCode);

    return Optional.ofNullable((UnitsModel) convertOne(query.getSingleResultOrNull(), false, UnitsModel.class));
  }

  /**
   * AOT generated implementation of {@link UnitsRepository#findByUnitName(java.lang.String)}.
   */
  public List<UnitsModel> findByUnitName(String unitName) {
    String queryString = "SELECT u FROM UnitsModel u WHERE u.unitName = :unitName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("unitName", unitName);

    return (List<UnitsModel>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link UnitsRepository#findByUnitNameOrStatus(java.lang.String,java.lang.String)}.
   */
  public List<UnitsModel> findByUnitNameOrStatus(String unitName, String status) {
    String queryString = "SELECT u FROM UnitsModel u WHERE u.unitName = :unitName OR u.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("unitName", unitName);
    query.setParameter("status", status);

    return (List<UnitsModel>) query.getResultList();
  }
}
