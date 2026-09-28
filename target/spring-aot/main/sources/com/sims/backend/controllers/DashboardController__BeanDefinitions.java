package com.sims.backend.controllers;

import com.sims.backend.services.DashboardService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DashboardController}.
 */
@Generated
public class DashboardController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'dashboardController'.
   */
  private static BeanInstanceSupplier<DashboardController> getDashboardControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DashboardController>forConstructor(DashboardService.class)
            .withGenerator((registeredBean, args) -> new DashboardController(args.get(0)));
  }

  /**
   * Get the bean definition for 'dashboardController'.
   */
  public static BeanDefinition getDashboardControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DashboardController.class);
    beanDefinition.setInstanceSupplier(getDashboardControllerInstanceSupplier());
    return beanDefinition;
  }
}
