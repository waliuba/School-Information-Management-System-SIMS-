package com.sims.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.DepartmentRequestDTO;
import com.sims.backend.dtos.DepartmentResponseDTO;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.mappers.DepartmentMapper;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentsRepository;

@Service
public class departmentService {

    private final DepartmentRepository departmentRepository;
    private final ClassRepository classRepository;
    private final StudentsRepository studentsRepository;
    private final EnrollmentsRepository enrollmentsRepository;

    public departmentService(
            DepartmentRepository departmentRepository,
            ClassRepository classRepository,
            StudentsRepository studentsRepository,
            EnrollmentsRepository enrollmentsRepository) {
        this.departmentRepository = departmentRepository;
        this.classRepository = classRepository;
        this.studentsRepository = studentsRepository;
        this.enrollmentsRepository = enrollmentsRepository;
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        return toDTOList(departmentRepository.findAll());
    }

    public DepartmentResponseDTO getDepartmentById(Long departmentId) {
        if (departmentId == null || departmentId <= 0) {
            return null;
        }

        return departmentRepository.findById(departmentId)
                .map(DepartmentMapper::toDTO)
                .orElse(null);
    }

    public DepartmentResponseDTO getDepartmentByName(String departmentName) {
        if (departmentName == null || departmentName.isBlank()) {
            return null;
        }

        return departmentRepository.findByDepartmentName(departmentName.trim())
                .map(DepartmentMapper::toDTO)
                .orElse(null);
    }

    public List<DepartmentResponseDTO> searchDepartmentsByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllDepartments();
        }

        String searchTerm = name.trim();
        return toDTOList(departmentRepository.findByDepartmentNameContainingIgnoreCase(searchTerm));
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest) {
        DepartmentModel department = DepartmentMapper.toEntity(departmentRequest);
        validateDepartment(department);

        String departmentName = department.getDepartmentName().trim();
        if (departmentRepository.existsByDepartmentName(departmentName)) {
            throw new BusinessRuleException("Department name already exists");
        }

        department.setDepartmentName(departmentName);
        return DepartmentMapper.toDTO(departmentRepository.save(department));
    }

    public DepartmentResponseDTO updateDepartment(Long departmentId, DepartmentRequestDTO departmentRequest) {
        if (departmentId == null || departmentId <= 0) {
            throw new IllegalArgumentException("Department id must be greater than zero");
        }

        if (!departmentRepository.existsById(departmentId)) {
            return null;
        }

        DepartmentModel department = DepartmentMapper.toEntity(departmentRequest);
        validateDepartment(department);
        department.setDepartmentId(departmentId);
        department.setDepartmentName(department.getDepartmentName().trim());
        return DepartmentMapper.toDTO(departmentRepository.save(department));
    }

    public boolean deleteDepartmentById(Long departmentId) {
        if (departmentId == null || departmentId <= 0 || !departmentRepository.existsById(departmentId)) {
            return false;
        }

        if (classRepository.existsByDepartmentModel_DepartmentId(departmentId)
                || studentsRepository.existsByDepartmentModel_DepartmentId(departmentId)
                || enrollmentsRepository.existsByDepartmentModel_DepartmentId(departmentId)) {
            throw new BusinessRuleException("Cannot delete department with existing classes, students or enrollments");
        }

        departmentRepository.deleteById(departmentId);
        return true;
    }

    private List<DepartmentResponseDTO> toDTOList(List<DepartmentModel> departments) {
        return departments.stream()
                .map(DepartmentMapper::toDTO)
                .toList();
    }

    private void validateDepartment(DepartmentModel department) {
        if (department == null) {
            throw new IllegalArgumentException("Department data is required");
        }
        if (department.getDepartmentName() == null || department.getDepartmentName().isBlank()) {
            throw new IllegalArgumentException("Department name is required");
        }
    }
}
