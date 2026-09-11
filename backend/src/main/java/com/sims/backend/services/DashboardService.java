package com.sims.backend.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sims.backend.enums.Role;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.CoursesRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.StudentsRepository;
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

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("totalStudents", studentsRepository.count());
        summary.put("totalTeachers", userRepository.countByRole(Role.TEACHER));
        summary.put("totalClasses", classRepository.count());
        summary.put("totalSubjects", coursesRepository.count());
        summary.put("totalDepartments", departmentRepository.count());
        return summary;
    }
}
