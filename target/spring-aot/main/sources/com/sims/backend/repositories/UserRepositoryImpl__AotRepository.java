package com.sims.backend.repositories;

import com.sims.backend.enums.Role;
import com.sims.backend.models.UserModel;
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
 * AOT generated JPA repository implementation for {@link UserRepository}.
 */
@Generated
public class UserRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public UserRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link UserRepository#countByRole(com.sims.backend.enums.Role)}.
   */
  public long countByRole(Role role) {
    String queryString = "SELECT COUNT(u) FROM UserModel u WHERE u.role = :role";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("role", role);

    return (Long) convertOne(query.getSingleResultOrNull(), false, Long.class);
  }

  /**
   * AOT generated implementation of {@link UserRepository#existsByEmail(java.lang.String)}.
   */
  public boolean existsByEmail(String email) {
    String queryString = "SELECT u.userId FROM UserModel u WHERE u.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link UserRepository#existsByUsername(java.lang.String)}.
   */
  public boolean existsByUsername(String username) {
    String queryString = "SELECT u.userId FROM UserModel u WHERE u.username = :username";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("username", username);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByEmail(java.lang.String)}.
   */
  public Optional<UserModel> findByEmail(String email) {
    String queryString = "SELECT u FROM UserModel u WHERE u.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);

    return Optional.ofNullable((UserModel) convertOne(query.getSingleResultOrNull(), false, UserModel.class));
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByUsername(java.lang.String)}.
   */
  public Optional<UserModel> findByUsername(String username) {
    String queryString = "SELECT u FROM UserModel u WHERE u.username = :username";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("username", username);

    return Optional.ofNullable((UserModel) convertOne(query.getSingleResultOrNull(), false, UserModel.class));
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByUsernameContainingIgnoreCase(java.lang.String)}.
   */
  public List<UserModel> findByUsernameContainingIgnoreCase(String username) {
    String queryString = "SELECT u FROM UserModel u WHERE UPPER(u.username) LIKE UPPER(:username) ESCAPE '\\'";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("username", "%%%s%%".formatted(username != null ? username.toUpperCase() : username));

    return (List<UserModel>) query.getResultList();
  }
}
