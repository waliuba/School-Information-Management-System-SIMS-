package com.sims.backend.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sims.backend.enums.Role;
import com.sims.backend.enums.Status;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.CourseUnits;
import com.sims.backend.models.Courses;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.EnrollmentsModel;
import com.sims.backend.models.StudentPerformanceModel;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.models.UnitsModel;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.CourseUnitsRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentPerformanceRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.repositories.UnitsRepository;
import com.sims.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private static final String[] DEPARTMENT_NAMES = {
            "Business", "Computing", "Engineering", "Health Sciences", "Education",
            "Agriculture", "Hospitality", "Social Sciences", "Law", "Media Studies"
    };
    private static final String[] UNIT_NAMES = {
            "Human Resources", "Business Management", "Finance", "Information Technology",
            "Electrical Engineering", "Community Health", "Teacher Education",
            "Agribusiness", "Hospitality Management", "Communication Studies"
    };
    private static final String[] SUBJECT_NAMES = {
            "Accounting", "Business Management", "Computer Studies", "Mathematics",
            "English", "Biology", "Agriculture", "Hospitality", "Civics", "Communication"
    };
    private static final String[] FIRST_NAMES = {
            "Amina", "Brian", "Carol", "Daniel", "Esther", "Faith", "George", "Hannah",
            "Isaac", "Janet", "Kevin", "Lucy", "Michael", "Naomi", "Oscar", "Patricia"
    };
    private static final String[] LAST_NAMES = {
            "Abdi", "Barasa", "Chebet", "Davis", "Elias", "Farah", "Gichuru", "Hassan",
            "Ibrahim", "Juma", "Kamau", "Lagat", "Maina", "Njeri", "Otieno", "Wambui"
    };

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final ClassRepository classRepository;
    private final CoursesRepository coursesRepository;
    private final CourseUnitsRepository courseUnitsRepository;
    private final UnitsRepository unitsRepository;
    private final StudentsRepository studentsRepository;
    private final EnrollmentsRepository enrollmentsRepository;
    private final StudentPerformanceRepository performanceRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUser("admin", "admin@sims.com", "Admin@123", Role.ADMIN);
        seedUser("teacher", "teacher@sims.com", "Teacher@123", Role.TEACHER);
        seedUser("student", "student@sims.com", "Student@123", Role.STUDENT);
        seedTeachers();

        List<DepartmentModel> departments = seedDepartments();
        List<UnitsModel> units = seedUnits();
        List<ClassModel> classes = seedClasses(departments);
        List<Courses> courses = seedSubjects();
        seedCourseUnits(courses, units);
        seedStudents(departments, classes, courses);
    }

    private void seedUser(String username, String email, String password, Role role) {
        UserModel user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(email).orElseGet(UserModel::new));
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);
        userRepository.save(user);
    }

    private void seedTeachers() {
        for (int index = 1; index <= 15; index++) {
            seedUser("teacher" + index, "teacher" + index + "@sims.com",
                    "Teacher@123", Role.TEACHER);
        }
    }

    private List<DepartmentModel> seedDepartments() {
        List<DepartmentModel> departments = new ArrayList<>();
        for (String name : DEPARTMENT_NAMES) {
            departments.add(departmentRepository.findByDepartmentName(name).orElseGet(() -> {
                DepartmentModel department = new DepartmentModel();
                department.setDepartmentName(name);
                return departmentRepository.save(department);
            }));
        }
        return departments;
    }

    private List<UnitsModel> seedUnits() {
        List<UnitsModel> units = new ArrayList<>();
        for (int index = 0; index < UNIT_NAMES.length; index++) {
            int unitIndex = index;
            String code = "UNIT" + String.format("%02d", index + 1);
            units.add(unitsRepository.findByUnitCode(code).orElseGet(() -> {
                UnitsModel unit = new UnitsModel();
                unit.setUnitName(UNIT_NAMES[unitIndex]);
                unit.setUnitCode(code);
                unit.setDescription(UNIT_NAMES[unitIndex] + " department unit");
                unit.setStatus("Active");
                return unitsRepository.save(unit);
            }));
        }
        return units;
    }

    private List<ClassModel> seedClasses(List<DepartmentModel> departments) {
        List<ClassModel> classes = new ArrayList<>();
        for (int stream = 1; stream <= 4; stream++) {
            for (String section : new String[] {"A", "B"}) {
                String name = "Form " + stream + section;
                ClassModel classModel = classRepository.findByClassName(name).orElseGet(ClassModel::new);
                classModel.setClassName(name);
                classModel.setAcademicYear("2026");
                classModel.setDepartmentModel(departments.get((stream - 1) % departments.size()));
                classes.add(classRepository.save(classModel));
            }
        }
        return classes;
    }

    private List<Courses> seedSubjects() {
        List<Courses> courses = new ArrayList<>();
        for (int index = 0; index < SUBJECT_NAMES.length; index++) {
            String code = "SUB" + String.format("%03d", index + 1);
            Courses course = coursesRepository.findByCourseCode(code).orElseGet(Courses::new);
            course.setCourseName(SUBJECT_NAMES[index]);
            course.setCourseCode(code);
            course.setDescription(SUBJECT_NAMES[index] + " subject");
            course.setDurationYears(1);
            course.setStatus(Status.Active);
            courses.add(coursesRepository.save(course));
        }
        return courses;
    }

    private void seedCourseUnits(List<Courses> courses, List<UnitsModel> units) {
        for (int index = 0; index < courses.size(); index++) {
            Courses course = courses.get(index);
            if (!courseUnitsRepository.existsByCourseId_CourseId(course.getCourseId())) {
                CourseUnits courseUnit = new CourseUnits();
                courseUnit.setCourseId(course);
                courseUnit.setUnitId(units.get(index % units.size()));
                courseUnit.setUnitDescription(course.getCourseName() + " course subject");
                courseUnit.setSemester("1");
                courseUnit.setYearofstudy("1");
                courseUnitsRepository.save(courseUnit);
            }
        }
    }

    private void seedStudents(
            List<DepartmentModel> departments,
            List<ClassModel> classes,
            List<Courses> courses) {
        for (int index = 1; index <= 150; index++) {
            String admissionNo = String.format("SIMS%04d", index);
            StudentsModel student = studentsRepository.findByAdmissionNo(admissionNo)
                    .orElseGet(StudentsModel::new);
            student.setAdmissionNo(admissionNo);
            student.setFirstName(FIRST_NAMES[(index - 1) % FIRST_NAMES.length]);
            student.setMiddleName("Student");
            student.setLastName(LAST_NAMES[(index - 1) % LAST_NAMES.length]);
            student.setGender(index % 2 == 0 ? "Female" : "Male");
            student.setDateOfBirth(LocalDate.of(2006 + (index % 5), (index % 12) + 1, (index % 27) + 1));
            student.setNationalId(String.format("ID%08d", index));
            student.setEmail("student" + index + "@sims.com");
            student.setPhone(String.format("0700%06d", index));
            student.setAddress("SIMS campus");
            student.setCounty("Nairobi");
            student.setAdmissionDate(LocalDate.of(2026, 1, 6));
            student.setClassModel(classes.get((index - 1) % classes.size()));
            student.setDepartmentModel(departments.get((index - 1) % departments.size()));
            student.setGuardianName("Guardian " + index);
            student.setGuardianPhone(String.format("0710%06d", index));
            student.setStatus(Status.Active.name());
            student = studentsRepository.save(student);

            Courses course = courses.get((index - 1) % courses.size());
            if (!enrollmentsRepository.existsByStudentsModel_StudentIdAndCourseModel_CourseId(
                    student.getStudentId(), course.getCourseId())) {
                EnrollmentsModel enrollment = new EnrollmentsModel();
                enrollment.setStudentsModel(student);
                enrollment.setDepartmentModel(student.getDepartmentModel());
                enrollment.setCourseModel(course);
                enrollment.setEnrollmentDate(LocalDate.of(2026, 1, 6));
                enrollment.setSemester(1);
                enrollmentsRepository.save(enrollment);
            }

            StudentPerformanceModel performance = performanceRepository
                    .findByStudentModel_StudentId(student.getStudentId()).orElseGet(StudentPerformanceModel::new);
            int score = 45 + ((index * 17) % 56);
            performance.setStudentModel(student);
            performance.setCourseModel(course);
            performance.setScore(BigDecimal.valueOf(score));
            performance.setGrade(gradeFor(score));
            performance.setPerformance(performanceFor(score));
            performanceRepository.save(performance);
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
