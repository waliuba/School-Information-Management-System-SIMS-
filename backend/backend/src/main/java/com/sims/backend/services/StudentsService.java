package com.sims.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.StudentsRequestDTO;
import com.sims.backend.dtos.StudentsResponseDTO;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.mappers.StudentMapper;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.EnrollmentsRepository;
import com.sims.backend.repositories.StudentsRepository;

@Service
public class StudentsService {

    private final StudentsRepository studentsRepository;
    private final ClassRepository classRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentsRepository enrollmentsRepository;

    public StudentsService(
            StudentsRepository studentsRepository,
            ClassRepository classRepository,
            DepartmentRepository departmentRepository,
            EnrollmentsRepository enrollmentsRepository) {
        this.studentsRepository = studentsRepository;
        this.classRepository = classRepository;
        this.departmentRepository = departmentRepository;
        this.enrollmentsRepository = enrollmentsRepository;
    }

    public List<StudentsResponseDTO> getAllStudents() {
        return toDTOList(studentsRepository.findAll());
    }

    public Optional<StudentsResponseDTO> getStudentById(Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student id must be greater than zero");
        }

        return studentsRepository.findById(studentId).map(StudentMapper::toDTO);
    }

    public List<StudentsResponseDTO> searchStudentsByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllStudents();
        }

        String searchTerm = name.trim();
        return toDTOList(studentsRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm,
                searchTerm
        ));
    }

    public StudentsResponseDTO saveStudent(StudentsRequestDTO studentRequest) {
        StudentsModel student = toEntity(studentRequest);
        validateStudent(student);
        return StudentMapper.toDTO(studentsRepository.save(student));
    }

    public StudentsResponseDTO createStudent(StudentsRequestDTO studentRequest) {
        StudentsModel student = toEntity(studentRequest);
        validateStudent(student);

        if (studentsRepository.existsByAdmissionNo(student.getAdmissionNo().trim())) {
            throw new BusinessRuleException("Admission number already exists");
        }

        if (student.getEmail() != null && !student.getEmail().isBlank()
                && studentsRepository.existsByEmail(student.getEmail().trim())) {
            throw new BusinessRuleException("Email already exists");
        }

        normalizeStudent(student);
        return StudentMapper.toDTO(studentsRepository.save(student));
    }

    public void deleteStudent(Long studentId) {
        if (studentId != null && studentId > 0) {
            studentsRepository.deleteById(studentId);
        }
    }

    public boolean deleteStudentById(Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student id must be greater than zero");
        }

        ensureStudentExists(studentId);
        if (enrollmentsRepository.existsByStudentsModel_StudentId(studentId)) {
            throw new BusinessRuleException("Cannot delete student with existing enrollments");
        }
        studentsRepository.deleteById(studentId);
        return true;
    }

    public StudentsResponseDTO updateStudent(Long studentId, StudentsRequestDTO studentRequest) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student id must be greater than zero");
        }

        ensureStudentExists(studentId);

        StudentsModel student = toEntity(studentRequest);
        validateStudent(student);
        normalizeStudent(student);
        if (studentsRepository.existsByAdmissionNoAndStudentIdNot(student.getAdmissionNo(), studentId)) {
            throw new BusinessRuleException("Admission number already exists");
        }
        if (student.getEmail() != null && !student.getEmail().isBlank()
                && studentsRepository.existsByEmailAndStudentIdNot(student.getEmail(), studentId)) {
            throw new BusinessRuleException("Email already exists");
        }
        student.setStudentId(studentId);
        return StudentMapper.toDTO(studentsRepository.save(student));
    }


    public List<StudentsResponseDTO> getStudentsByClassId(Long classId) {
        if (classId == null || classId <= 0) {
            return List.of(); // Return an empty list if classId is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassId(classId));
    }

    public List<StudentsResponseDTO> getStudentsByClassName(String className) {
        if (className == null || className.isBlank()) {
            return List.of(); // Return an empty list if className is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassName(className.trim()));
    }

    public List<StudentsResponseDTO> getStudentsByStatus(String status) {
        if (status == null || status.isBlank()) {
            return List.of(); // Return an empty list if status is invalid
        }

        return toDTOList(studentsRepository.findByStatus(status.trim()));
    }

    public List<StudentsResponseDTO> getStudentsByClassIdAndStatus(Long classId, String status) {
        if (classId == null || classId <= 0 || status == null || status.isBlank()) {
            return List.of(); // Return an empty list if classId or status is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassIdAndStatus(classId, status.trim()));
    }

    public List<StudentsResponseDTO> getStudentsByClassNameAndStatus(String className, String status) {
        if (className == null || className.isBlank() || status == null || status.isBlank()) {
            return List.of(); // Return an empty list if className or status is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassNameAndStatus(className.trim(), status.trim()));
    }

    public List<StudentsResponseDTO> getStudentsByClassIdAndStatusAndName(Long classId, String status, String name) {
        if (classId == null || classId <= 0 || status == null || status.isBlank() || name == null || name.isBlank()) {
            return List.of(); // Return an empty list if any parameter is invalid
        }

        String searchTerm = name.trim();
        return toDTOList(studentsRepository.findByClassModel_ClassIdAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                classId,
                status.trim(),
                searchTerm,
                searchTerm
        ));
    }


    public List<StudentsResponseDTO> getStudentsByClassNameAndStatusAndName(String className, String status, String name) {
        if (className == null || className.isBlank() || status == null || status.isBlank() || name == null || name.isBlank()) {
            return List.of(); // Return an empty list if any parameter is invalid
        }

        String searchTerm = name.trim();
        return toDTOList(studentsRepository.findByClassModel_ClassNameAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                className.trim(),
                status.trim(),
                searchTerm,
                searchTerm
        ));
    }

    public List<StudentsResponseDTO> getStudents(String name, Long classId, String className, Long departmentId, String status) {
        return toDTOList(studentsRepository.searchStudents(
                normalizeSearch(name),
                classId,
                normalizeSearch(className),
                departmentId,
                normalizeSearch(status)
        ));
    }

    private StudentsModel toEntity(StudentsRequestDTO studentRequest) {
        if (studentRequest == null) {
            throw new IllegalArgumentException("Student data is required");
        }

        ClassModel classModel = null;
        if (studentRequest.getClassId() != null) {
            classModel = classRepository.findById(studentRequest.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        }

        DepartmentModel departmentModel = null;
        if (studentRequest.getDepartmentId() != null) {
            departmentModel = departmentRepository.findById(studentRequest.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        }

        if (classModel != null && departmentModel != null
                && classModel.getDepartmentModel() != null
                && !classModel.getDepartmentModel().getDepartmentId().equals(departmentModel.getDepartmentId())) {
            throw new BusinessRuleException("Class does not belong to the selected department");
        }

        return StudentMapper.toEntity(studentRequest, classModel, departmentModel);
    }

    private List<StudentsResponseDTO> toDTOList(List<StudentsModel> students) {
        return students.stream()
                .map(StudentMapper::toDTO)
                .toList();
    }

    private void validateStudent(StudentsModel student) {
        if (student == null) {
            throw new IllegalArgumentException("Student data is required");
        }
        if (student.getAdmissionNo() == null || student.getAdmissionNo().isBlank()) {
            throw new IllegalArgumentException("Admission number is required");
        }
        if (student.getFirstName() == null || student.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (student.getLastName() == null || student.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (student.getClassModel() == null || student.getClassModel().getClassId() == null) {
            throw new IllegalArgumentException("Class is required");
        }
        if (student.getDepartmentModel() == null || student.getDepartmentModel().getDepartmentId() == null) {
            throw new IllegalArgumentException("Department is required");
        }
    }

    private void normalizeStudent(StudentsModel student) {
        student.setAdmissionNo(student.getAdmissionNo().trim());
        student.setFirstName(student.getFirstName().trim());
        student.setLastName(student.getLastName().trim());
        if (student.getEmail() != null) {
            student.setEmail(student.getEmail().trim());
        }
        if (student.getStatus() != null) {
            student.setStatus(student.getStatus().trim());
        }
    }

    private void ensureStudentExists(Long studentId) {
        if (!studentsRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found");
        }
    }

    private String normalizeSearch(String value) {
        return value == null ? null : value.trim();
    }

}
