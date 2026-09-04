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
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.enums.Role;
import com.sims.backend.services.RoleService;




@RestController
@RequestMapping("/api/roles")

public class RoleController {

    private final RoleService roleservice;

    public RoleController(RoleService roleservice) {
        this.roleservice = roleservice;
    }
    
    @GetMapping("/search")

    public ResponseEntity<ApiResponse<List<Role>>> searchRolesByName(@RequestParam String name) {
        List<Role> roles = roleservice.searchRolesByName(name);

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No roles found with the given name");
        }

        return ResponseEntity.ok(ApiResponse.of("Roles retrieved successfully", roles));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> getRoles(@RequestParam(required = false) String name) {
        List<Role> roles = roleservice.searchRolesByName(name);

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No roles found");
        }

        return ResponseEntity.ok(ApiResponse.of("Roles retrieved successfully", roles));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long roleId) {
        Role role = roleservice.getRoleById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return ResponseEntity.ok(ApiResponse.of("Role retrieved successfully", role));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Role>> createRole(@RequestBody Role role) {
        Role createdRole = roleservice.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("Role created successfully", createdRole));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Role>> updateRole(
            @PathVariable Long roleId,
            @RequestBody Role role) {
        Role  updatedRole = roleservice.updateRole(roleId, role);

        if (updatedRole == null) {
            throw new ResourceNotFoundException("Role not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Role updated successfully", updatedRole));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long roleId) {
        boolean deleted = roleservice.deleteRoleById(roleId);

        if (!deleted) {
            throw new ResourceNotFoundException("Role not found");
        }

        return ResponseEntity.ok(ApiResponse.of("Role deleted successfully", null));
    }
}
