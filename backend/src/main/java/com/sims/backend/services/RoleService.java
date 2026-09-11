package com.sims.backend.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.enums.Role;

@Service
public class RoleService {

    public List<Role> searchRolesByName(String name) {
        if (name == null || name.isBlank()) {
            return Arrays.asList(Role.values());
        }

        String query = name.trim().toLowerCase();
        return Arrays.stream(Role.values())
                .filter(role -> role.name().toLowerCase().contains(query))
                .toList();
    }

    public Optional<Role> getRoleById(Long roleId) {
        if (roleId == null || roleId <= 0 || roleId > Role.values().length) {
            return Optional.empty();
        }

        return Optional.of(Role.values()[Math.toIntExact(roleId) - 1]);
    }

    public Role createRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }
        return role;
    }

    public Role updateRole(Long roleId, Role role) {
        if (roleId == null || roleId <= 0 || roleId > Role.values().length) {
            return null;
        }
        if (role == null) {
            return null;
        }
        return role;
    }

    public boolean deleteRoleById(Long roleId) {
        return roleId != null && roleId > 0 && roleId <= Role.values().length;
    }
}
