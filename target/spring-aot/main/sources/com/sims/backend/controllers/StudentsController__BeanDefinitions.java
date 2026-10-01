package com.sims.backend.controllers;

import com.sims.backend.services.StudentsService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StudentsController}.
 */
@Generated
public class StudentsController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'studentsController'.
   */
  private static BeanInstanceSupplier<StudentsController> getStudentsControllerInstanceSupplier() {
    return BeanInstanceSupplier.<StudentsController>forConstructor(StudentsService.class)
            .withGenerator((registeredBean, args) -> new StudentsController(args.get(0)));
  }

  /**
   * Get the bean definition for 'studentsController'.
   */
  public static BeanDefinition getStudentsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StudentsController.class);
    beanDefinition.setInstanceSupplier(getStudentsControllerInstanceSupplier());
    return beanDefinition;
  }
}
