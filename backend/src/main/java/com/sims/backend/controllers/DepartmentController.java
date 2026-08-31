package com.sims.backend.controllers;

import java.util.List;

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

import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.dtos.DepartmentRequestDTO;
import com.sims.backend.dtos.DepartmentResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
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
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getDepartments(@RequestParam(required = false) String name) {
        List<DepartmentResponseDTO> departments = departmentService.searchDepartmentsByName(name);

        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("No departments found");
        }

        return ResponseEntity.ok(ApiResponse.of("Departments retrieved successfully", departments));
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable Long departmentId) {
        DepartmentResponseDTO department = departmentService.getDepartmentById(departmentId);

        if (department == null) {
            throw new ResourceNotFoundException("Department not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Department retrieved successfully", department));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(@Valid @RequestBody DepartmentRequestDTO department) {
        DepartmentResponseDTO createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Department created successfully", createdDepartment));
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentRequestDTO department) {
        DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(departmentId, department);

        if (updatedDepartment == null) {
            throw new ResourceNotFoundException("Department not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Department updated successfully", updatedDepartment));
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long departmentId) {
        boolean deleted = departmentService.deleteDepartmentById(departmentId);

        if (!deleted) {
            throw new ResourceNotFoundException("Department not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Department deleted successfully", null));
    }
}
