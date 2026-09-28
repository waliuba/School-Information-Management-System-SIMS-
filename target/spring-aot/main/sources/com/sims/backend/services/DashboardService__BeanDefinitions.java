package com.sims.backend.services;

import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.repositories.UserRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DashboardService}.
 */
@Generated
public class DashboardService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'dashboardService'.
   */
  private static BeanInstanceSupplier<DashboardService> getDashboardServiceInstanceSupplier() {
    return BeanInstanceSupplier.<DashboardService>forConstructor(StudentsRepository.class, UserRepository.class, ClassRepository.class, CoursesRepository.class, DepartmentRepository.class)
            .withGenerator((registeredBean, args) -> new DashboardService(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4)));
  }

  /**
   * Get the bean definition for 'dashboardService'.
   */
  public static BeanDefinition getDashboardServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DashboardService.class);
    beanDefinition.setInstanceSupplier(getDashboardServiceInstanceSupplier());
    return beanDefinition;
  }
}
