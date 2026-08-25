package com.sims.backend.mappers;

import com.sims.backend.dto.DepartmentRequestDTO;
import com.sims.backend.dto.DepartmentResponseDTO;
import com.sims.backend.models.DepartmentModel;

public class DepartmentMapper {

    public static DepartmentResponseDTO toDTO(DepartmentModel department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        return dto;
    }

    public static DepartmentModel toEntity(DepartmentRequestDTO dto) {
        DepartmentModel department = new DepartmentModel();
        department.setDepartmentName(dto.getDepartmentName());
        return department;
    }
}
