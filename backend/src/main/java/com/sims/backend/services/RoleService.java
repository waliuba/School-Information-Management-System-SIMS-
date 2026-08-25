package com.sims.backend.services;

import com.sims.backend.models.RoleModel;
import com.sims.backend.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
   
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleModel> searchRolesByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllRoles();
        }
        return roleRepository.findByRoleName(name);
    }

    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<RoleModel> getRoleById(Long roleId) {
        if (roleId == null || roleId <= 0) {
            return Optional.empty();
        }
        return roleRepository.findById(roleId);
    }

    public RoleModel createRole(RoleModel role) {
        validateRole(role);
        String roleName = role.getRoleName().trim();

        if (roleRepository.existsByRoleName(roleName)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        role.setRoleName(roleName);
        return roleRepository.save(role);
    }

    public RoleModel updateRole(Long roleId, RoleModel role) {
        if (roleId == null || roleId <= 0) {
            throw new IllegalArgumentException("Role id must be greater than zero");
        }

        if (!roleRepository.existsById(roleId)) {
            return null;
        }

        validateRole(role);
        role.setRoleId(roleId);
        role.setRoleName(role.getRoleName().trim());
        return roleRepository.save(role);
    }

    public boolean deleteRoleById(Long roleId) {
        if (roleId == null || roleId <= 0 || !roleRepository.existsById(roleId)) {
            return false;
        }

        roleRepository.deleteById(roleId);
        return true;
    }

    private void validateRole(RoleModel role) {
        if (role == null) {
            throw new IllegalArgumentException("Role data is required");
        }
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }
    }
}
