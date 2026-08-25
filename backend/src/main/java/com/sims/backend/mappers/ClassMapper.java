package com.sims.backend.mappers;

import com.sims.backend.dto.ClassRequestDTO;
import com.sims.backend.dto.ClassResponseDTO;
import com.sims.backend.models.ClassModel;
import com.sims.backend.models.DepartmentModel;

public class ClassMapper {

    public static ClassResponseDTO toDTO(ClassModel classModel) {
        ClassResponseDTO dto = new ClassResponseDTO();
        dto.setClassId(classModel.getClassId());
        dto.setClassName(classModel.getClassName());
        dto.setAcademicYear(classModel.getAcademicYear());
        if (classModel.getDepartmentModel() != null) {
            dto.setDepartmentId(classModel.getDepartmentModel().getDepartmentId());
        }
        return dto;
    }

    public static ClassModel toEntity(ClassRequestDTO dto, DepartmentModel departmentModel) {
        ClassModel classModel = new ClassModel();
        classModel.setClassName(dto.getClassName());
        classModel.setAcademicYear(dto.getAcademicYear());
        classModel.setDepartmentModel(departmentModel);
        return classModel;
    }
}
