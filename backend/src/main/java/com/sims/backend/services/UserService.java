package com.sims.backend.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sims.backend.dtos.user.UserRequestDTO;
import com.sims.backend.dtos.user.UserResponseDTO;
import com.sims.backend.dtos.user.UserUpdateRequestDTO;
import com.sims.backend.mappers.UserMapper;
import com.sims.backend.models.UserModel;
import com.sims.backend.enums.Role;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        UserModel user = UserMapper.toEntity(request);

        
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        UserModel savedUser = userRepository.save(user);

        return UserMapper.toDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public List<UserResponseDTO> getTeachers() {
        return userRepository.findByRole(Role.TEACHER).stream().map(UserMapper::toDTO).toList();
    }

    public UserResponseDTO getTeacherById(Long id) {
        UserModel teacher = userRepository.findById(id).filter(u -> u.getRole() == Role.TEACHER)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return UserMapper.toDTO(teacher);
    }

    public UserResponseDTO updateTeacher(Long id, String username, String email, Boolean enabled) {
        UserModel teacher = userRepository.findById(id).filter(u -> u.getRole() == Role.TEACHER)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        if (username != null && userRepository.findByUsername(username).filter(u -> !u.getUserId().equals(id)).isPresent())
            throw new BusinessRuleException("Username already exists");
        if (email != null && userRepository.findByEmail(email).filter(u -> !u.getUserId().equals(id)).isPresent())
            throw new BusinessRuleException("Email already exists");
        if (username != null) teacher.setUsername(username);
        if (email != null) teacher.setEmail(email);
        if (enabled != null) teacher.setEnabled(enabled);
        return UserMapper.toDTO(userRepository.save(teacher));
    }

    public void deleteTeacher(Long id) {
        UserModel teacher = userRepository.findById(id).filter(u -> u.getRole() == Role.TEACHER)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        userRepository.delete(teacher);
    }

    public UserResponseDTO getUserById(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        return UserMapper.toDTO(user);
    }

    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO request, String authenticatedUsername) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getUsername().equals(authenticatedUsername)) {
            throw new BusinessRuleException("Use My Profile to update your own account details");
        }
        if (user.getEnabled() && user.getRole() == Role.ADMIN
                && (request.getRole() != Role.ADMIN || !request.getEnabled())
                && userRepository.countByRoleAndEnabled(Role.ADMIN, true) <= 1) {
            throw new BusinessRuleException("At least one enabled administrator account must remain");
        }

        userRepository.findByUsername(request.getUsername())
                .filter(existing -> !existing.getUserId().equals(userId))
                .ifPresent(existing -> { throw new BusinessRuleException("Username already exists"); });
        userRepository.findByEmail(request.getEmail())
                .filter(existing -> !existing.getUserId().equals(userId))
                .ifPresent(existing -> { throw new BusinessRuleException("Email already exists"); });

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled());
        return UserMapper.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long userId, String authenticatedUsername) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getUsername().equals(authenticatedUsername)) {
            throw new BusinessRuleException("You cannot delete your own signed-in account");
        }
        if (user.getEnabled() && user.getRole() == Role.ADMIN
                && userRepository.countByRoleAndEnabled(Role.ADMIN, true) <= 1) {
            throw new BusinessRuleException("At least one enabled administrator account must remain");
        }
        userRepository.delete(user);
    }
}
