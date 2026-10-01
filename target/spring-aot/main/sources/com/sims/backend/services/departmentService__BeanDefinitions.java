package com.sims.backend.services;

import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentsRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link departmentService}.
 */
@Generated
public class departmentService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'departmentService'.
   */
  private static BeanInstanceSupplier<departmentService> getDepartmentServiceInstanceSupplier() {
    return BeanInstanceSupplier.<departmentService>forConstructor(DepartmentRepository.class, ClassRepository.class, StudentsRepository.class, EnrollmentsRepository.class)
            .withGenerator((registeredBean, args) -> new departmentService(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'departmentService'.
   */
  public static BeanDefinition getDepartmentServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(departmentService.class);
    beanDefinition.setInstanceSupplier(getDepartmentServiceInstanceSupplier());
    return beanDefinition;
  }
}
