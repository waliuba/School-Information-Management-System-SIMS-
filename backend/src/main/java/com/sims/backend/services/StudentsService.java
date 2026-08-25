package com.sims.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.dto.StudentRequestDTO;
import com.sims.backend.dto.StudentResponseDTO;
import com.sims.backend.mappers.StudentMapper;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.repositories.ClassRepository;
import com.sims.backend.repositories.DepartmentRepository;
import com.sims.backend.repositories.StudentsRepository;

@Service
public class StudentsService {

    private final StudentsRepository studentsRepository;
    private final ClassRepository classRepository;
    private final DepartmentRepository departmentRepository;

    public StudentsService(
            StudentsRepository studentsRepository,
            ClassRepository classRepository,
            DepartmentRepository departmentRepository) {
        this.studentsRepository = studentsRepository;
        this.classRepository = classRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<StudentResponseDTO> getAllStudents() {
        return toDTOList(studentsRepository.findAll());
    }

    public Optional<StudentResponseDTO> getStudentById(Long studentId) {
        if (studentId == null || studentId <= 0) {
            return Optional.empty();
        }

        return studentsRepository.findById(studentId).map(StudentMapper::toDTO);
    }

    public List<StudentResponseDTO> searchStudentsByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllStudents();
        }

        String searchTerm = name.trim();
        return toDTOList(studentsRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm,
                searchTerm
        ));
    }

    public StudentResponseDTO saveStudent(StudentRequestDTO studentRequest) {
        StudentsModel student = toEntity(studentRequest);
        validateStudent(student);
        return StudentMapper.toDTO(studentsRepository.save(student));
    }

    public StudentResponseDTO createStudent(StudentRequestDTO studentRequest) {
        StudentsModel student = toEntity(studentRequest);
        validateStudent(student);

        if (studentsRepository.existsByAdmissionNo(student.getAdmissionNo().trim())) {
            throw new IllegalArgumentException("Admission number already exists");
        }

        if (student.getEmail() != null && !student.getEmail().isBlank()
                && studentsRepository.existsByEmail(student.getEmail().trim())) {
            throw new IllegalArgumentException("Email already exists");
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
        if (studentId == null || studentId <= 0 || !studentsRepository.existsById(studentId)) {
            return false;
        }

        studentsRepository.deleteById(studentId);
        return true;
    }

    public StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO studentRequest) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student id must be greater than zero");
        }

        if (!studentsRepository.existsById(studentId)) {
            return null;
        }

        StudentsModel student = toEntity(studentRequest);
        validateStudent(student);
        normalizeStudent(student);
        student.setStudentId(studentId);
        return StudentMapper.toDTO(studentsRepository.save(student));
    }


    public List<StudentResponseDTO> getStudentsByClassId(Long classId) {
        if (classId == null || classId <= 0) {
            return List.of(); // Return an empty list if classId is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassId(classId));
    }

    public List<StudentResponseDTO> getStudentsByClassName(String className) {
        if (className == null || className.isBlank()) {
            return List.of(); // Return an empty list if className is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassName(className.trim()));
    }

    public List<StudentResponseDTO> getStudentsByStatus(String status) {
        if (status == null || status.isBlank()) {
            return List.of(); // Return an empty list if status is invalid
        }

        return toDTOList(studentsRepository.findByStatus(status.trim()));
    }

    public List<StudentResponseDTO> getStudentsByClassIdAndStatus(Long classId, String status) {
        if (classId == null || classId <= 0 || status == null || status.isBlank()) {
            return List.of(); // Return an empty list if classId or status is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassIdAndStatus(classId, status.trim()));
    }

    public List<StudentResponseDTO> getStudentsByClassNameAndStatus(String className, String status) {
        if (className == null || className.isBlank() || status == null || status.isBlank()) {
            return List.of(); // Return an empty list if className or status is invalid
        }

        return toDTOList(studentsRepository.findByClassModel_ClassNameAndStatus(className.trim(), status.trim()));
    }

    public List<StudentResponseDTO> getStudentsByClassIdAndStatusAndName(Long classId, String status, String name) {
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


    public List<StudentResponseDTO> getStudentsByClassNameAndStatusAndName(String className, String status, String name) {
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

    public List<StudentResponseDTO> getStudents(String name, Long classId, String className, Long departmentId, String status) {
        if (classId != null && status != null && !status.isBlank() && name != null && !name.isBlank()) {
            return getStudentsByClassIdAndStatusAndName(classId, status, name);
        }

        if (className != null && !className.isBlank() && status != null && !status.isBlank()
                && name != null && !name.isBlank()) {
            return getStudentsByClassNameAndStatusAndName(className, status, name);
        }

        if (classId != null && status != null && !status.isBlank()) {
            return getStudentsByClassIdAndStatus(classId, status);
        }

        if (className != null && !className.isBlank() && status != null && !status.isBlank()) {
            return getStudentsByClassNameAndStatus(className, status);
        }

        if (classId != null) {
            return getStudentsByClassId(classId);
        }

        if (className != null && !className.isBlank()) {
            return getStudentsByClassName(className);
        }

        if (departmentId != null && departmentId > 0) {
            return toDTOList(studentsRepository.findByDepartmentModel_DepartmentId(departmentId));
        }

        if (status != null && !status.isBlank()) {
            return getStudentsByStatus(status);
        }

        return searchStudentsByName(name);
    }

    private StudentsModel toEntity(StudentRequestDTO studentRequest) {
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

        return StudentMapper.toEntity(studentRequest, classModel, departmentModel);
    }

    private List<StudentResponseDTO> toDTOList(List<StudentsModel> students) {
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

}
