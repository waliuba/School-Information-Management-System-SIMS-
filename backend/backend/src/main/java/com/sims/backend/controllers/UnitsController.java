package com.sims.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.dtos.UnitRequestDTO;
import com.sims.backend.dtos.UnitResponseDTO;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.services.UnitsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/units")
public class UnitsController {

    private final UnitsService unitsService;

    public UnitsController(UnitsService unitsService) {
        this.unitsService = unitsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnitResponseDTO>>> getUnits(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String unitCode) {
        List<UnitResponseDTO> units;

        if (unitCode != null && !unitCode.isBlank()) {
            units = unitsService.getUnitsByUnitCode(unitCode);
        } else if (name != null && !name.isBlank()) {
            units = unitsService.getUnitsByUnitName(name);
        } else if (status != null && !status.isBlank()) {
            units = unitsService.getUnitsByStatus(status);
        } else {
            units = unitsService.getAllUnits();
        }

        if (units == null || units.isEmpty()) {
            throw new ResourceNotFoundException("No units found");
        }

        return ResponseEntity.ok(ApiResponse.of("Units retrieved successfully", units));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponseDTO>> getUnitById(@PathVariable Long id) {
        UnitResponseDTO unit = unitsService.getUnitById(id);

        if (unit == null) {
            throw new ResourceNotFoundException("Unit not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Unit retrieved successfully", unit));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UnitResponseDTO>> createUnit(@Valid @RequestBody UnitRequestDTO unit) {
        UnitResponseDTO savedUnit = unitsService.createUnit(unit);

        if (savedUnit == null) {
            throw new IllegalArgumentException("Unable to create unit");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Unit created successfully", savedUnit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitResponseDTO>> updateUnit(@PathVariable Long id, @Valid @RequestBody UnitRequestDTO unit) {
        UnitResponseDTO updatedUnit = unitsService.updateUnit(id, unit);

        if (updatedUnit == null) {
            throw new ResourceNotFoundException("Unit not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Unit updated successfully", updatedUnit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUnit(@PathVariable Long id) {
        boolean deleted = unitsService.deleteUnitId(id);

        if (!deleted) {
            throw new ResourceNotFoundException("Unit not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Unit deleted successfully", null));
    }


    



}
