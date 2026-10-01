package com.sims.backend.config;

import com.sims.backend.repositories.UserRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link DataSeeder}.
 */
@Generated
public class DataSeeder__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'dataSeeder'.
   */
  private static BeanInstanceSupplier<DataSeeder> getDataSeederInstanceSupplier() {
    return BeanInstanceSupplier.<DataSeeder>forConstructor(UserRepository.class, PasswordEncoder.class)
            .withGenerator((registeredBean, args) -> new DataSeeder(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'dataSeeder'.
   */
  public static BeanDefinition getDataSeederBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataSeeder.class);
    beanDefinition.setInstanceSupplier(getDataSeederInstanceSupplier());
    return beanDefinition;
  }
}
