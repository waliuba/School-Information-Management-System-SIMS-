package com.sims.backend.services;

import com.sims.backend.models.Role;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    public List<Role> searchRolesByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllRoles();
        }
        String normalizedName = name.trim().toUpperCase();
        return Arrays.stream(Role.values())
                .filter(role -> role.name().contains(normalizedName))
                .toList();
    }

    public List<Role> getAllRoles() {
        return Arrays.asList(Role.values());
    }

    public Optional<Role> getRoleById(Long roleId) {
        if (roleId == null || roleId <= 0) {
            return Optional.empty();
        }
        Role[] roles = Role.values();
        int index = roleId.intValue() - 1;
        if (index < 0 || index >= roles.length) {
            return Optional.empty();
        }
        return Optional.of(roles[index]);
    }

    public Role createRole(Role role) {
        validateRole(role);
        return role;
    }

    public Role updateRole(Long roleId, Role role) {
        if (roleId == null || roleId <= 0) {
            throw new IllegalArgumentException("Role id must be greater than zero");
        }

        if (getRoleById(roleId).isEmpty()) {
            return null;
        }

        validateRole(role);
        return role;
    }

    public boolean deleteRoleById(Long roleId) {
        return getRoleById(roleId).isPresent();
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role data is required");
        }
    }
}
