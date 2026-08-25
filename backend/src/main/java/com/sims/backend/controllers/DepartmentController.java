package com.sims.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dto.DepartmentRequestDTO;
import com.sims.backend.dto.DepartmentResponseDTO;
import com.sims.backend.services.departmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final departmentService departmentService;

    public DepartmentController(departmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<?> getDepartments(@RequestParam(required = false) String name) {
        List<DepartmentResponseDTO> departments = departmentService.searchDepartmentsByName(name);

        if (departments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No departments found"));
        }

        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long departmentId) {
        DepartmentResponseDTO department = departmentService.getDepartmentById(departmentId);

        if (department == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Department not found"));
        }

        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepartmentRequestDTO department) {
        try {
            DepartmentResponseDTO createdDepartment = departmentService.createDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentRequestDTO department) {
        try {
            DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(departmentId, department);

            if (updatedDepartment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Department not found"));
            }

            return ResponseEntity.ok(updatedDepartment);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long departmentId) {
        boolean deleted = departmentService.deleteDepartmentById(departmentId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Department not found"));
        }

        return ResponseEntity.noContent().build();
    }
}
