package com.sims.backend.controllers;

import com.sims.backend.services.CourseUnitsService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CourseUnitsController}.
 */
@Generated
public class CourseUnitsController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'courseUnitsController'.
   */
  private static BeanInstanceSupplier<CourseUnitsController> getCourseUnitsControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<CourseUnitsController>forConstructor(CourseUnitsService.class)
            .withGenerator((registeredBean, args) -> new CourseUnitsController(args.get(0)));
  }

  /**
   * Get the bean definition for 'courseUnitsController'.
   */
  public static BeanDefinition getCourseUnitsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CourseUnitsController.class);
    beanDefinition.setInstanceSupplier(getCourseUnitsControllerInstanceSupplier());
    return beanDefinition;
  }
}
