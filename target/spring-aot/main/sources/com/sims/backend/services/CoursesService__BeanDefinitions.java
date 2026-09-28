package com.sims.backend.services;

import com.sims.backend.repositories.CourseUnitsRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CoursesService}.
 */
@Generated
public class CoursesService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'coursesService'.
   */
  private static BeanInstanceSupplier<CoursesService> getCoursesServiceInstanceSupplier() {
    return BeanInstanceSupplier.<CoursesService>forConstructor(CoursesRepository.class, EnrollmentsRepository.class, CourseUnitsRepository.class)
            .withGenerator((registeredBean, args) -> new CoursesService(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'coursesService'.
   */
  public static BeanDefinition getCoursesServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CoursesService.class);
    beanDefinition.setInstanceSupplier(getCoursesServiceInstanceSupplier());
    return beanDefinition;
  }
}
