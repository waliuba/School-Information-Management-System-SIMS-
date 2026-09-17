package com.sims.backend.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.students.StudentPerformanceResponseDTO;
import com.sims.backend.enums.Role;
import com.sims.backend.models.StudentPerformanceModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.repositories.StudentPerformanceRepository;
import com.sims.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentsRepository studentsRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final CoursesRepository coursesRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentPerformanceRepository performanceRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("totalStudents", studentsRepository.count());
        summary.put("totalTeachers", userRepository.countByRole(Role.TEACHER));
        summary.put("totalClasses", classRepository.count());
        summary.put("totalSubjects", coursesRepository.count());
        summary.put("totalDepartments", departmentRepository.count());
        return summary;
    }

    public Map<String, Object> getPerformance() {
        List<StudentPerformanceResponseDTO> rows = performanceRepository.findAllByOrderByScoreDesc()
                .stream()
                .map(this::toPerformanceDTO)
                .collect(Collectors.toList());

        Map<String, Object> performance = new LinkedHashMap<>();
        performance.put("rows", rows);
        performance.put("marksDistribution", List.of(
                distribution("0-49", "#ef4444", rows, 0, 49),
                distribution("50-59", "#f59e0b", rows, 50, 59),
                distribution("60-69", "#eab308", rows, 60, 69),
                distribution("70-79", "#22c55e", rows, 70, 79),
                distribution("80-100", "#16a34a", rows, 80, 100)
        ));
        performance.put("gradeDistribution", List.of(
                gradeDistribution("A", "#16a34a", rows),
                gradeDistribution("B", "#22c55e", rows),
                gradeDistribution("C", "#eab308", rows),
                gradeDistribution("D", "#f59e0b", rows),
                gradeDistribution("E", "#ef4444", rows)
        ));
        return performance;
    }

    private StudentPerformanceResponseDTO toPerformanceDTO(StudentPerformanceModel performance) {
        StudentPerformanceResponseDTO dto = new StudentPerformanceResponseDTO();
        var student = performance.getStudentModel();
        dto.setStudentId(student.getStudentId());
        dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        dto.setAdmissionNo(student.getAdmissionNo());
        dto.setClassId(student.getClassModel() == null ? null : student.getClassModel().getClassId());
        dto.setClassName(student.getClassModel() == null ? "Unassigned" : student.getClassModel().getClassName());
        dto.setAverageScore(performance.getScore());
        dto.setGrade(performance.getGrade());
        dto.setPerformance(performance.getPerformance());
        return dto;
    }

    private Map<String, Object> distribution(
            String label,
            String color,
            List<StudentPerformanceResponseDTO> rows,
            int minimum,
            int maximum) {
        long value = rows.stream()
                .filter(row -> row.getAverageScore().doubleValue() >= minimum
                        && row.getAverageScore().doubleValue() <= maximum)
                .count();
        return Map.of("label", label, "color", color, "value", value);
    }

    private Map<String, Object> gradeDistribution(
            String label,
            String color,
            List<StudentPerformanceResponseDTO> rows) {
        long value = rows.stream().filter(row -> label.equals(row.getGrade())).count();
        return Map.of("label", label, "color", color, "value", value);
    }
}
