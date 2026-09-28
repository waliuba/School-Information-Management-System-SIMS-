package com.sims.backend.controllers;

import com.sims.backend.services.RoleService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RoleController}.
 */
@Generated
public class RoleController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'roleController'.
   */
  private static BeanInstanceSupplier<RoleController> getRoleControllerInstanceSupplier() {
    return BeanInstanceSupplier.<RoleController>forConstructor(RoleService.class)
            .withGenerator((registeredBean, args) -> new RoleController(args.get(0)));
  }

  /**
   * Get the bean definition for 'roleController'.
   */
  public static BeanDefinition getRoleControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RoleController.class);
    beanDefinition.setInstanceSupplier(getRoleControllerInstanceSupplier());
    return beanDefinition;
  }
}
