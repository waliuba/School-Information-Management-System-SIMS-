package com.sims.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.models.StudentPerformanceModel;
import com.sims.backend.repositories.StudentPerformanceRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResultsController {

    private final StudentPerformanceRepository performanceRepository;

    @GetMapping({"/results", "/exams"})
    public ResponseEntity<List<Map<String, Object>>> getResults() {
        List<Map<String, Object>> results = performanceRepository.findAllByOrderByScoreDesc()
                .stream()
                .map(this::toResult)
                .toList();
        return ResponseEntity.ok(results);
    }

    private Map<String, Object> toResult(StudentPerformanceModel performance) {
        return Map.of(
                "studentId", performance.getStudentModel().getStudentId(),
                "studentName", performance.getStudentModel().getFirstName() + " "
                        + performance.getStudentModel().getLastName(),
                "admissionNo", performance.getStudentModel().getAdmissionNo(),
                "subject", performance.getCourseModel().getCourseName(),
                "score", performance.getScore(),
                "grade", performance.getGrade(),
                "performance", performance.getPerformance());
    }
}
