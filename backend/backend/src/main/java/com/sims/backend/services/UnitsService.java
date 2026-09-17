package com.sims.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sims.backend.dtos.UnitRequestDTO;
import com.sims.backend.dtos.UnitResponseDTO;
import com.sims.backend.mappers.UnitMapper;
import com.sims.backend.models.UnitsModel;
import com.sims.backend.repositories.UnitsRepository;

@Service
public class UnitsService {
    private final UnitsRepository unitsRepository;

    public UnitsService(UnitsRepository unitsRepository) {
        this.unitsRepository = unitsRepository;
    }

    public List<UnitResponseDTO> getAllUnits() {
        return toDTOList(unitsRepository.findAll());
    }

    public UnitResponseDTO saveUnit(UnitRequestDTO unitRequest) {
        UnitsModel unit = UnitMapper.toEntity(unitRequest);
        normalizeUnit(unit);
        return UnitMapper.toDTO(unitsRepository.save(unit));
    }

    public UnitResponseDTO createUnit(UnitRequestDTO unitRequest) {
        return saveUnit(unitRequest);
    }

    public void deleteUnit(Long unitId) {
        if (unitId != null && unitId > 0) {
            unitsRepository.deleteById(unitId);
        }
    }

    public List<UnitResponseDTO> searchUnitsByNameOrStatus(String name, String status) {
        if ((name == null || name.isBlank()) && (status == null || status.isBlank())) {
            return getAllUnits();
        }

        String searchTerm = (name != null) ? name.trim() : "";
        String searchStatus = (status != null) ? status.trim() : "";

        return toDTOList(unitsRepository.findByUnitNameOrStatus(searchTerm, searchStatus));
    }

    public List<UnitResponseDTO> getUnitsByUnitCode(String unitCode) {
        if (unitCode == null || unitCode.isBlank()) {
            return List.of();
        }

        return unitsRepository.findByUnitCode(unitCode.trim())
                .map(unit -> List.of(UnitMapper.toDTO(unit)))
                .orElse(List.of());
    }

    public List<UnitResponseDTO> getUnitsByUnitName(String unitName) {
        if (unitName == null || unitName.isBlank()) {
            return List.of();
        }

        return toDTOList(unitsRepository.findByUnitName(unitName.trim()));
    }

    public List<UnitResponseDTO> getUnitsByStatus(String status) {
        if (status == null || status.isBlank()) {
            return List.of();
        }

        return toDTOList(unitsRepository.findByStatus(status.trim()));
    }

    public UnitResponseDTO getUnitById(Long unitId) {
        if (unitId == null || unitId <= 0) {
            return null;
        }

        return unitsRepository.findById(unitId)
                .map(UnitMapper::toDTO)
                .orElse(null);
    }

    public UnitResponseDTO updateUnit(Long unitId, UnitRequestDTO unitRequest) {
        if (unitId == null || unitId <= 0 || unitRequest == null) {
            return null;
        }

        if (!unitsRepository.existsById(unitId)) {
            return null;
        }

        UnitsModel unit = UnitMapper.toEntity(unitRequest);
        normalizeUnit(unit);
        unit.setUnitId(unitId);
        return UnitMapper.toDTO(unitsRepository.save(unit));
    }

    public boolean deleteUnitId(Long unitId) {
        if (unitId == null || unitId <= 0 || !unitsRepository.existsById(unitId)) {
            return false;
        }

        unitsRepository.deleteById(unitId);
        return true;
    }

    private List<UnitResponseDTO> toDTOList(List<UnitsModel> units) {
        return units.stream()
                .map(UnitMapper::toDTO)
                .toList();
    }

    private void normalizeUnit(UnitsModel unit) {
        unit.setUnitName(unit.getUnitName().trim());
        unit.setUnitCode(unit.getUnitCode().trim());
        unit.setDescription(unit.getDescription().trim());
        unit.setStatus(unit.getStatus().trim());
    }
}
