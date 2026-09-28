package com.sims.backend.controllers;

import com.sims.backend.services.EnrollmentsService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link EnrollmentController}.
 */
@Generated
public class EnrollmentController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'enrollmentController'.
   */
  private static BeanInstanceSupplier<EnrollmentController> getEnrollmentControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<EnrollmentController>forConstructor(EnrollmentsService.class)
            .withGenerator((registeredBean, args) -> new EnrollmentController(args.get(0)));
  }

  /**
   * Get the bean definition for 'enrollmentController'.
   */
  public static BeanDefinition getEnrollmentControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(EnrollmentController.class);
    beanDefinition.setInstanceSupplier(getEnrollmentControllerInstanceSupplier());
    return beanDefinition;
  }
}
