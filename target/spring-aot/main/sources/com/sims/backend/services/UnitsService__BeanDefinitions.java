package com.sims.backend.services;

import com.sims.backend.repositories.UnitsRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link UnitsService}.
 */
@Generated
public class UnitsService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'unitsService'.
   */
  private static BeanInstanceSupplier<UnitsService> getUnitsServiceInstanceSupplier() {
    return BeanInstanceSupplier.<UnitsService>forConstructor(UnitsRepository.class)
            .withGenerator((registeredBean, args) -> new UnitsService(args.get(0)));
  }

  /**
   * Get the bean definition for 'unitsService'.
   */
  public static BeanDefinition getUnitsServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(UnitsService.class);
    beanDefinition.setInstanceSupplier(getUnitsServiceInstanceSupplier());
    return beanDefinition;
  }
}
