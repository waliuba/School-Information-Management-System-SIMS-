package com.sims.backend.controllers;

import com.sims.backend.services.ClassService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClassController}.
 */
@Generated
public class ClassController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'classController'.
   */
  private static BeanInstanceSupplier<ClassController> getClassControllerInstanceSupplier() {
    return BeanInstanceSupplier.<ClassController>forConstructor(ClassService.class)
            .withGenerator((registeredBean, args) -> new ClassController(args.get(0)));
  }

  /**
   * Get the bean definition for 'classController'.
   */
  public static BeanDefinition getClassControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClassController.class);
    beanDefinition.setInstanceSupplier(getClassControllerInstanceSupplier());
    return beanDefinition;
  }
}
