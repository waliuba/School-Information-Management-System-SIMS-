package com.sims.backend.mappers;

import com.sims.backend.dtos.StudentsRequestDTO;
import com.sims.backend.dtos.StudentsResponseDTO;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.DepartmentModel;
import com.sims.backend.models.StudentsModel;

public class StudentMapper {

    public static StudentsResponseDTO toDTO(StudentsModel student) {

        StudentsResponseDTO dto = new StudentsResponseDTO();

        dto.setStudentId(student.getStudentId());
        dto.setAdmissionNo(student.getAdmissionNo());
        dto.setFirstName(student.getFirstName());
        dto.setMiddleName(student.getMiddleName());
        dto.setLastName(student.getLastName());
        dto.setGender(student.getGender());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setNationalId(student.getNationalId());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setAddress(student.getAddress());
        dto.setCounty(student.getCounty());
        dto.setAdmissionDate(student.getAdmissionDate());

        // Get class ID from ClassModel
        if (student.getClassModel() != null) {
            dto.setClassId(student.getClassModel().getClassId());
        }

        // Get department ID from DepartmentModel
        if (student.getDepartmentModel() != null) {
            dto.setDepartmentId(
                student.getDepartmentModel().getDepartmentId()
            );
        }

        dto.setGuardianName(student.getGuardianName());
        dto.setGuardianPhone(student.getGuardianPhone());
        dto.setStatus(student.getStatus());

        return dto;
    }

    public static StudentsModel toEntity(
            StudentsRequestDTO dto,
            ClassModel classModel,
            DepartmentModel departmentModel) {
        StudentsModel student = new StudentsModel();

        student.setAdmissionNo(dto.getAdmissionNo());
        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setLastName(dto.getLastName());
        student.setGender(dto.getGender());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setNationalId(dto.getNationalId());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setAddress(dto.getAddress());
        student.setCounty(dto.getCounty());
        student.setAdmissionDate(dto.getAdmissionDate());
        student.setClassModel(classModel);
        student.setDepartmentModel(departmentModel);
        student.setGuardianName(dto.getGuardianName());
        student.setGuardianPhone(dto.getGuardianPhone());
        student.setStatus(dto.getStatus());

        return student;
    }
}
