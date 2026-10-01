package com.sims.backend.services;

import com.sims.backend.repositories.UserRepository;
import com.sims.backend.security.JwtService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link AuthService}.
 */
@Generated
public class AuthService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'authService'.
   */
  private static BeanInstanceSupplier<AuthService> getAuthServiceInstanceSupplier() {
    return BeanInstanceSupplier.<AuthService>forConstructor(UserRepository.class, PasswordEncoder.class, JwtService.class)
            .withGenerator((registeredBean, args) -> new AuthService(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'authService'.
   */
  public static BeanDefinition getAuthServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthService.class);
    beanDefinition.setInstanceSupplier(getAuthServiceInstanceSupplier());
    return beanDefinition;
  }
}
