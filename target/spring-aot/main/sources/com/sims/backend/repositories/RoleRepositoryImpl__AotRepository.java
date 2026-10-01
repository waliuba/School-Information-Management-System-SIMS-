package com.sims.backend.repositories;

import com.sims.backend.enums.Role;
import com.sims.backend.models.UserModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link RoleRepository}.
 */
@Generated
public class RoleRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public RoleRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link RoleRepository#findByRole(com.sims.backend.enums.Role)}.
   */
  public List<UserModel> findByRole(Role role) {
    String queryString = "SELECT u FROM UserModel u WHERE u.role = :role";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("role", role);

    return (List<UserModel>) query.getResultList();
  }
}
