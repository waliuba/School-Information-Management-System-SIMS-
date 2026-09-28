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
 * Bean definitions for {@link ClassService}.
 */
@Generated
public class ClassService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'classService'.
   */
  private static BeanInstanceSupplier<ClassService> getClassServiceInstanceSupplier() {
    return BeanInstanceSupplier.<ClassService>forConstructor(ClassRepository.class, DepartmentRepository.class, StudentsRepository.class, EnrollmentsRepository.class)
            .withGenerator((registeredBean, args) -> new ClassService(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'classService'.
   */
  public static BeanDefinition getClassServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClassService.class);
    beanDefinition.setInstanceSupplier(getClassServiceInstanceSupplier());
    return beanDefinition;
  }
}
