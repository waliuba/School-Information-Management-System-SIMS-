package com.sims.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.EnrollmentsModel;
import com.sims.backend.models.StudentsModel;

class EnrollmentMapperTest {

    @Test
    void mapsStudentAndDepartmentNamesToEnrollmentResponse() {
        StudentsModel student = new StudentsModel();
        student.setFirstName("Amina");
        student.setMiddleName("Wanjiku");
        student.setLastName("Abdi");

        DepartmentModel department = new DepartmentModel();
        department.setDepartmentName("Computing");

        EnrollmentsModel enrollment = new EnrollmentsModel();
        enrollment.setEnrollmentId(12L);
        enrollment.setStudentsModel(student);
        enrollment.setDepartmentModel(department);

        var response = EnrollmentMapper.toDTO(enrollment);

        assertThat(response.getEnrollmentId()).isEqualTo(12L);
        assertThat(response.getStudentName()).isEqualTo("Amina Wanjiku Abdi");
        assertThat(response.getDepartmentName()).isEqualTo("Computing");
    }
}
