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
 * Bean definitions for {@link StudentsService}.
 */
@Generated
public class StudentsService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'studentsService'.
   */
  private static BeanInstanceSupplier<StudentsService> getStudentsServiceInstanceSupplier() {
    return BeanInstanceSupplier.<StudentsService>forConstructor(StudentsRepository.class, ClassRepository.class, DepartmentRepository.class, EnrollmentsRepository.class)
            .withGenerator((registeredBean, args) -> new StudentsService(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'studentsService'.
   */
  public static BeanDefinition getStudentsServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StudentsService.class);
    beanDefinition.setInstanceSupplier(getStudentsServiceInstanceSupplier());
    return beanDefinition;
  }
}
