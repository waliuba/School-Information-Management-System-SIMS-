package com.sims.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sims.backend.models.StudentsModel;

public interface StudentsRepository extends JpaRepository<StudentsModel, Long> {

    Optional<StudentsModel> findByAdmissionNo(String admissionNo);

    List<StudentsModel> findByFirstName(String firstName);

    List<StudentsModel> findByFirstNameAndLastName(String firstName, String lastName);

    List<StudentsModel> findByLastName(String lastName);

    Optional<StudentsModel> findByEmail(String email);

    List<StudentsModel> findByClassModel_ClassId(Long classId);

    List<StudentsModel> findByDepartmentModel_DepartmentId(Long departmentId);

    List<StudentsModel> findByStatus(String status);

    List<StudentsModel> findByFirstNameContainingIgnoreCase(String firstName);

    List<StudentsModel> findByLastNameContainingIgnoreCase(String lastName);

    List<StudentsModel> findByGender(String gender);

    List<StudentsModel> findByClassModel_ClassName(String className);

    List<StudentsModel> findByClassModel_ClassIdAndStatus(Long classId, String status);

    List<StudentsModel> findByClassModel_ClassNameAndStatus(String className, String status);

    List<StudentsModel> findByClassModel_ClassIdAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            Long classId,
            String status,
            String firstName,
            String lastName
    );

    List<StudentsModel> findByClassModel_ClassNameAndStatusAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String className,
            String status,
            String firstName,
            String lastName
    );


    List<StudentsModel> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    boolean existsByAdmissionNo(String admissionNo);

    boolean existsByEmail(String email);

    

} 

