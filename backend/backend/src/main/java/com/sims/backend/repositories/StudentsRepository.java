package com.sims.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    boolean existsByAdmissionNoAndStudentIdNot(String admissionNo, Long studentId);

    boolean existsByEmailAndStudentIdNot(String email, Long studentId);

    boolean existsByClassModel_ClassId(Long classId);

    boolean existsByDepartmentModel_DepartmentId(Long departmentId);

    @Query("""
            select s from StudentsModel s
            where (:name is null or :name = '' or
                lower(s.firstName) like lower(concat('%', :name, '%')) or
                lower(s.lastName) like lower(concat('%', :name, '%')) or
                lower(s.admissionNo) like lower(concat('%', :name, '%')))
            and (:classId is null or s.classModel.classId = :classId)
            and (:className is null or :className = '' or lower(s.classModel.className) = lower(:className))
            and (:departmentId is null or s.departmentModel.departmentId = :departmentId)
            and (:status is null or :status = '' or lower(s.status) = lower(:status))
            """)
    List<StudentsModel> searchStudents(
            @Param("name") String name,
            @Param("classId") Long classId,
            @Param("className") String className,
            @Param("departmentId") Long departmentId,
            @Param("status") String status
    );

} 
