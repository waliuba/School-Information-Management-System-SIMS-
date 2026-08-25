package com.sims.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getUsers(String username, String status, Long roleId) {
        if (username != null && !username.isBlank()) {
            return userRepository.findByUsernameContainingIgnoreCase(username.trim());
        }

        if (status != null && !status.isBlank()) {
            return userRepository.findByStatus(status.trim());
        }

        if (roleId != null && roleId > 0) {
            return userRepository.findByRoleModel_RoleId(roleId);
        }

        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(Long userId) {
        if (userId == null || userId <= 0) {
            return Optional.empty();
        }

        return userRepository.findById(userId);
    }

    public Optional<UserModel> getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email.trim());
    }

    public UserModel createUser(UserModel user) {
        validateUser(user);

        if (userRepository.existsByUsername(user.getUsername().trim())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail().trim())) {
            throw new IllegalArgumentException("Email already exists");
        }

        normalizeUser(user);
        return userRepository.save(user);
    }

    public UserModel updateUser(Long userId, UserModel user) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User id must be greater than zero");
        }

        if (!userRepository.existsById(userId)) {
            return null;
        }

        validateUser(user);
        normalizeUser(user);
        user.setUserId(userId);
        return userRepository.save(user);
    }

    public boolean deleteUserById(Long userId) {
        if (userId == null || userId <= 0 || !userRepository.existsById(userId)) {
            return false;
        }

        userRepository.deleteById(userId);
        return true;
    }

    private void validateUser(UserModel user) {
        if (user == null) {
            throw new IllegalArgumentException("User data is required");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getRoleModel() == null || user.getRoleModel().getRoleId() <= 0) {
            throw new IllegalArgumentException("Role is required");
        }
        if (user.getStatus() == null || user.getStatus().isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }
        if (user.getCreatedAt() == null || user.getCreatedAt().isBlank()) {
            throw new IllegalArgumentException("Created at is required");
        }
    }

    private void normalizeUser(UserModel user) {
        user.setUsername(user.getUsername().trim());
        user.setFirstName(user.getFirstName().trim());
        user.setEmail(user.getEmail().trim());
        user.setStatus(user.getStatus().trim());
        user.setCreatedAt(user.getCreatedAt().trim());
    }
}
