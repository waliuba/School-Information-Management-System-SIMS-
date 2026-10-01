package com.sims.backend.controllers;

import com.sims.backend.services.CoursesService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CoursesController}.
 */
@Generated
public class CoursesController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'coursesController'.
   */
  private static BeanInstanceSupplier<CoursesController> getCoursesControllerInstanceSupplier() {
    return BeanInstanceSupplier.<CoursesController>forConstructor(CoursesService.class)
            .withGenerator((registeredBean, args) -> new CoursesController(args.get(0)));
  }

  /**
   * Get the bean definition for 'coursesController'.
   */
  public static BeanDefinition getCoursesControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CoursesController.class);
    beanDefinition.setInstanceSupplier(getCoursesControllerInstanceSupplier());
    return beanDefinition;
  }
}
