package com.sims.backend.services;

import com.sims.backend.repositories.CourseUnitsRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.UnitsRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CourseUnitsService}.
 */
@Generated
public class CourseUnitsService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'courseUnitsService'.
   */
  private static BeanInstanceSupplier<CourseUnitsService> getCourseUnitsServiceInstanceSupplier() {
    return BeanInstanceSupplier.<CourseUnitsService>forConstructor(CourseUnitsRepository.class, CoursesRepository.class, UnitsRepository.class)
            .withGenerator((registeredBean, args) -> new CourseUnitsService(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'courseUnitsService'.
   */
  public static BeanDefinition getCourseUnitsServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CourseUnitsService.class);
    beanDefinition.setInstanceSupplier(getCourseUnitsServiceInstanceSupplier());
    return beanDefinition;
  }
}
