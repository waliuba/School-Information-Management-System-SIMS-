package com.sims.backend.controllers;

import com.sims.backend.services.UnitsService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link UnitsController}.
 */
@Generated
public class UnitsController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'unitsController'.
   */
  private static BeanInstanceSupplier<UnitsController> getUnitsControllerInstanceSupplier() {
    return BeanInstanceSupplier.<UnitsController>forConstructor(UnitsService.class)
            .withGenerator((registeredBean, args) -> new UnitsController(args.get(0)));
  }

  /**
   * Get the bean definition for 'unitsController'.
   */
  public static BeanDefinition getUnitsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(UnitsController.class);
    beanDefinition.setInstanceSupplier(getUnitsControllerInstanceSupplier());
    return beanDefinition;
  }
}
