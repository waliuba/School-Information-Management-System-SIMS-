package com.sims.backend.services;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RoleService}.
 */
@Generated
public class RoleService__BeanDefinitions {
  /**
   * Get the bean definition for 'roleService'.
   */
  public static BeanDefinition getRoleServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RoleService.class);
    beanDefinition.setInstanceSupplier(RoleService::new);
    return beanDefinition;
  }
}
