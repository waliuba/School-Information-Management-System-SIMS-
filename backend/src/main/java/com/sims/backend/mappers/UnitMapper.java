package com.sims.backend.mappers;

import com.sims.backend.dto.UnitRequestDTO;
import com.sims.backend.dto.UnitResponseDTO;
import com.sims.backend.models.UnitsModel;

public class UnitMapper {

    public static UnitResponseDTO toDTO(UnitsModel unit) {
        UnitResponseDTO dto = new UnitResponseDTO();
        dto.setUnitId(unit.getUnitId());
        dto.setUnitName(unit.getUnitName());
        dto.setUnitCode(unit.getUnitCode());
        dto.setDescription(unit.getDescription());
        dto.setStatus(unit.getStatus());
        return dto;
    }

    public static UnitsModel toEntity(UnitRequestDTO dto) {
        UnitsModel unit = new UnitsModel();
        unit.setUnitName(dto.getUnitName());
        unit.setUnitCode(dto.getUnitCode());
        unit.setDescription(dto.getDescription());
        unit.setStatus(dto.getStatus());
        return unit;
    }
}
