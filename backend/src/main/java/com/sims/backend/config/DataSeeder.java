package com.sims.backend.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import com.sims.backend.enums.Role;
import com.sims.backend.enums.Status;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.Courses;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.StudentPerformanceModel;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.StudentPerformanceRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final ClassRepository classRepository;
    private final CoursesRepository coursesRepository;
    private final StudentsRepository studentsRepository;
    private final StudentPerformanceRepository performanceRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUser("admin", "admin@sims.com", "Admin@123", Role.ADMIN);
        seedUser("teacher", "teacher@sims.com", "Teacher@123", Role.TEACHER);
        seedUser("student", "student@sims.com", "Student@123", Role.STUDENT);
        seedStudentsAndPerformance();
    }

    private void seedUser(String username, String email, String password, Role role) {
        UserModel user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(email).orElseGet(UserModel::new));

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);

        userRepository.saveAndFlush(user);
    }

    private void seedStudentsAndPerformance() {
        DepartmentModel department = departmentRepository.findByDepartmentName("General Studies")
                .orElseGet(() -> {
                    DepartmentModel model = new DepartmentModel();
                    model.setDepartmentName("General Studies");
                    return departmentRepository.save(model);
                });

        ClassModel classModel = classRepository.findByClassId(1L).orElseGet(() -> {
            ClassModel model = new ClassModel();
            model.setClassName("Form 1A");
            model.setAcademicYear("2026");
            model.setDepartmentModel(department);
            return classRepository.save(model);
        });

        Courses course = coursesRepository.findByCourseCode("GEN101").orElseGet(() -> {
            Courses model = new Courses();
            model.setCourseName("General Studies");
            model.setCourseCode("GEN101");
            model.setDescription("General studies assessment");
            model.setDurationYears(1);
            model.setStatus(Status.Active);
            return coursesRepository.save(model);
        });

        List<String> firstNames = List.of(
                "Amina", "Brian", "Carol", "Daniel", "Esther",
                "Faith", "George", "Hannah", "Isaac", "Janet");
        List<String> lastNames = List.of(
                "Abdi", "Barasa", "Chebet", "Davis", "Elias",
                "Farah", "Gichuru", "Hassan", "Ibrahim", "Juma");

        for (int index = 1; index <= 100; index++) {
            String admissionNo = String.format("SIMS%04d", index);
            StudentsModel student = studentsRepository.findByAdmissionNo(admissionNo).orElseGet(StudentsModel::new);
            student.setAdmissionNo(admissionNo);
            student.setFirstName(firstNames.get((index - 1) % firstNames.size()));
            student.setMiddleName("Student");
            student.setLastName(lastNames.get((index - 1) % lastNames.size()));
            student.setGender(index % 2 == 0 ? "Female" : "Male");
            student.setDateOfBirth(LocalDate.of(2008 + (index % 4), (index % 12) + 1, (index % 27) + 1));
            student.setNationalId(String.format("ID%08d", index));
            student.setEmail("student" + index + "@sims.com");
            student.setPhone(String.format("0700%06d", index));
            student.setAddress("SIMS campus");
            student.setCounty("Nairobi");
            student.setAdmissionDate(LocalDate.of(2026, 1, 6));
            student.setClassModel(classModel);
            student.setDepartmentModel(department);
            student.setGuardianName("Guardian " + index);
            student.setGuardianPhone(String.format("0710%06d", index));
            student.setStatus(Status.Active);
            student = studentsRepository.save(student);

            if (performanceRepository.findByStudentModel_StudentId(student.getStudentId()).isEmpty()) {
                int score = 45 + ((index * 17) % 56);
                StudentPerformanceModel performance = new StudentPerformanceModel();
                performance.setStudentModel(student);
                performance.setCourseModel(course);
                performance.setScore(BigDecimal.valueOf(score));
                performance.setGrade(gradeFor(score));
                performance.setPerformance(performanceFor(score));
                performanceRepository.save(performance);
            }
        }
    }

    private String gradeFor(int score) {
        if (score >= 80) return "A";
        if (score >= 70) return "B";
        if (score >= 60) return "C";
        if (score >= 50) return "D";
        return "E";
    }

    private String performanceFor(int score) {
        if (score >= 80) return "Excellent";
        if (score >= 70) return "Good";
        if (score >= 60) return "Average";
        if (score >= 50) return "Needs Improvement";
        return "At Risk";
    }
}
