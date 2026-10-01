package com.sims.backend.controllers;

import com.sims.backend.services.departmentService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DepartmentController}.
 */
@Generated
public class DepartmentController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'departmentController'.
   */
  private static BeanInstanceSupplier<DepartmentController> getDepartmentControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DepartmentController>forConstructor(departmentService.class)
            .withGenerator((registeredBean, args) -> new DepartmentController(args.get(0)));
  }

  /**
   * Get the bean definition for 'departmentController'.
   */
  public static BeanDefinition getDepartmentControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DepartmentController.class);
    beanDefinition.setInstanceSupplier(getDepartmentControllerInstanceSupplier());
    return beanDefinition;
  }
}
