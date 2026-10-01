package com.sims.backend.services;

import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentsRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link EnrollmentsService}.
 */
@Generated
public class EnrollmentsService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'enrollmentsService'.
   */
  private static BeanInstanceSupplier<EnrollmentsService> getEnrollmentsServiceInstanceSupplier() {
    return BeanInstanceSupplier.<EnrollmentsService>forConstructor(EnrollmentsRepository.class, StudentsRepository.class, DepartmentRepository.class, CoursesRepository.class)
            .withGenerator((registeredBean, args) -> new EnrollmentsService(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'enrollmentsService'.
   */
  public static BeanDefinition getEnrollmentsServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(EnrollmentsService.class);
    beanDefinition.setInstanceSupplier(getEnrollmentsServiceInstanceSupplier());
    return beanDefinition;
  }
}
