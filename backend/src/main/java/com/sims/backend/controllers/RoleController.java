package com.sims.backend.controllers;

import java.util.List;
import java.util.Map;

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
 
import com.sims.backend.models.RoleModel;
import com.sims.backend.services.RoleService;




@RestController
@RequestMapping("/api/roles")

public class RoleController {

    private final RoleService roleservice;

    public RoleController(RoleService roleservice) {
        this.roleservice = roleservice;
    }
    
    @GetMapping("/search")

    public ResponseEntity<?> searchRolesByName(@RequestParam String name) {
        List<RoleModel> roles = roleservice.searchRolesByName(name);

        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No roles found with the given name"));
        }

        return ResponseEntity.ok(roles);
    }

    @GetMapping
    public ResponseEntity<?> getRoles(@RequestParam(required = false) String name) {
        List<RoleModel> roles = roleservice.searchRolesByName(name);

        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No roles found"));
        }

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
        if (roleId == null || roleId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Role id must be greater than zero"));
        }

        return roleservice.getRoleById(roleId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Role not found")));
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleModel role) {
        try {
            RoleModel createdRole = roleservice.createRole(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long roleId,
            @RequestBody RoleModel role) {
        try {
            RoleModel updatedRole = roleservice.updateRole(roleId, role);

            if (updatedRole == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Role not found"));
            }

            return ResponseEntity.ok(updatedRole);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        boolean deleted = roleservice.deleteRoleById(roleId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Role not found"));
        }

        return ResponseEntity.noContent().build();
    }
}
