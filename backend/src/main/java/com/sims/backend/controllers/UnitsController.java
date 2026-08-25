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

import com.sims.backend.dto.UnitRequestDTO;
import com.sims.backend.dto.UnitResponseDTO;
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
    public ResponseEntity<?> getUnits(
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
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(units);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUnitById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Unit ID is required.");
        }

        UnitResponseDTO unit = unitsService.getUnitById(id);

        if (unit == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(unit);
    }

    @PostMapping
    public ResponseEntity<?> createUnit(@Valid @RequestBody UnitRequestDTO unit) {
        if (unit == null) {
            return ResponseEntity.badRequest().body("Unit data is required.");
        }

        UnitResponseDTO savedUnit = unitsService.createUnit(unit);

        if (savedUnit == null) {
            return ResponseEntity.badRequest().body("Unable to create unit.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUnit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @Valid @RequestBody UnitRequestDTO unit) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Unit ID is required.");
        }

        if (unit == null) {
            return ResponseEntity.badRequest().body("Unit data is required.");
        }

        UnitResponseDTO updatedUnit = unitsService.updateUnit(id, unit);

        if (updatedUnit == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUnit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Unit ID is required.");
        }

        boolean deleted = unitsService.deleteUnitId(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    



}
