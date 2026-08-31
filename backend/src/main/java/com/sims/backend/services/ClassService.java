package com.sims.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.ClassRequestDTO;
import com.sims.backend.dtos.ClassResponseDTO;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.mappers.ClassMapper;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentsRepository;

@Service
public class ClassService {

    public final ClassRepository classRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentsRepository studentsRepository;
    private final EnrollmentsRepository enrollmentsRepository;

    public ClassService(
            ClassRepository classRepository,
            DepartmentRepository departmentRepository,
            StudentsRepository studentsRepository,
            EnrollmentsRepository enrollmentsRepository) {
        this.classRepository = classRepository;
        this.departmentRepository = departmentRepository;
        this.studentsRepository = studentsRepository;
        this.enrollmentsRepository = enrollmentsRepository;
    }

    public List<ClassResponseDTO> getAllClasses() {
        if (classRepository.count() == 0) {
            return List.of();
        }

        return toDTOList(classRepository.findAll());
    }

    public List<ClassResponseDTO> getClasses(String name, String academicYear, Long departmentId) {
        if (departmentId != null && departmentId > 0 && academicYear != null && !academicYear.isBlank()) {
            return toDTOList(classRepository.findByDepartmentModel_departmentIdAndAcademicYear(
                    departmentId,
                    academicYear.trim()
            ));
        }

        if (departmentId != null && departmentId > 0) {
            return classRepository.findByDepartmentModel_departmentId(departmentId)
                    .map(classModel -> List.of(ClassMapper.toDTO(classModel)))
                    .orElse(List.of());
        }

        if (academicYear != null && !academicYear.isBlank()) {
            return toDTOList(classRepository.findByAcademicYear(academicYear.trim()));
        }

        if (name != null && !name.isBlank()) {
            return searchClassesByClassName(name);
        }

        return getAllClasses();
    }

    public Optional<ClassResponseDTO> getClassById(Long classId) {
        if (classId == null) {
            throw new IllegalArgumentException("classid cannot be null");
        }

        return classRepository.findById(classId).map(ClassMapper::toDTO);
    }

    public List<ClassResponseDTO> searchClassesByClassName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("need  names to search");
        }

        return toDTOList(classRepository.findByClassNameContainingIgnoreCase(name));
    }

    public ClassResponseDTO createClass(ClassRequestDTO classRequest) {
        ClassModel classModel = toEntity(classRequest);
        validateClass(classModel);
        return ClassMapper.toDTO(classRepository.save(classModel));
    }

    public void deleteClass(Long classId) {
        if (classId == null) {
            throw new IllegalArgumentException("classId cannot be null");
        }
        classRepository.deleteById(classId);
    }

    public boolean deleteClassById(Long classId) {
        if (classId == null || classId <= 0 || !classRepository.existsById(classId)) {
            return false;
        }

        if (studentsRepository.existsByClassModel_ClassId(classId)
                || enrollmentsRepository.existsByStudentsModel_ClassModel_ClassId(classId)) {
            throw new BusinessRuleException("Cannot delete class with existing students or enrollments");
        }

        classRepository.deleteById(classId);
        return true;
    }

    public ClassResponseDTO updateClass(Long classId, ClassRequestDTO classRequest) {
        if (classId == null) {
            throw new IllegalArgumentException("classId cannot be null");
        }
        if (!classRepository.existsById(classId)) {
            throw new IllegalArgumentException("Class not found");
        }

        ClassModel classModel = toEntity(classRequest);
        validateClass(classModel);
        classModel.setClassId(classId);
        return ClassMapper.toDTO(classRepository.save(classModel));
    }

    private ClassModel toEntity(ClassRequestDTO classRequest) {
        if (classRequest == null) {
            throw new IllegalArgumentException("Class data is required");
        }

        DepartmentModel department = departmentRepository.findById(classRequest.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        return ClassMapper.toEntity(classRequest, department);
    }

    private List<ClassResponseDTO> toDTOList(List<ClassModel> classes) {
        return classes.stream()
                .map(ClassMapper::toDTO)
                .toList();
    }

    private void validateClass(ClassModel classModel) {
        if (classModel == null) {
            throw new IllegalArgumentException("Class data is required");
        }
        if (classModel.getClassName() == null || classModel.getClassName().trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        if (classModel.getAcademicYear() == null || classModel.getAcademicYear().trim().isEmpty()) {
            throw new IllegalArgumentException("Academic year cannot be null or empty");
        }
        if (classModel.getDepartmentModel() == null || classModel.getDepartmentModel().getDepartmentId() == null) {
            throw new IllegalArgumentException("Department is required");
        }
    }
}
