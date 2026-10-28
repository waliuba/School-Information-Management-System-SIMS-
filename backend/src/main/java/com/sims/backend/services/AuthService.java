package com.sims.backend.services;

import com.sims.backend.dtos.authentication.AuthResponseDTO;
import com.sims.backend.dtos.authentication.ChangePasswordRequestDTO;
import com.sims.backend.dtos.authentication.LoginRequestDTO;
import com.sims.backend.dtos.authentication.RegisterRequestDTO;
import com.sims.backend.dtos.authentication.UpdateProfileRequestDTO;
import com.sims.backend.dtos.user.UserResponseDTO;
import com.sims.backend.mappers.UserMapper;
import com.sims.backend.models.UserModel;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.repositories.UserRepository;
import com.sims.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditService auditService;

    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserModel user = UserModel.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                // Public registration must never assign an administrative role.
                .role(com.sims.backend.enums.Role.STUDENT)
                .enabled(true)
                .build();

        UserModel savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        String identifier = request.getUsername();
        UserModel user = (identifier != null && identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByUsername(identifier))
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new BadCredentialsException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return buildAuthResponse(user);
    }

    public UserResponseDTO getCurrentUser(String username) {

        UserModel user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        return UserMapper.toDTO(user);
    }

    @Transactional
    public UserResponseDTO updateCurrentUser(String username, UpdateProfileRequestDTO request) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userRepository.findByEmail(request.getEmail())
                .filter(existingUser -> !existingUser.getUserId().equals(user.getUserId()))
                .ifPresent(existingUser -> {
                    throw new BusinessRuleException("Email already exists");
                });

        user.setEmail(request.getEmail());
        UserModel saved = userRepository.save(user);
        auditService.record(username, "PROFILE_EMAIL_UPDATED", "user:" + user.getUserId(),
                "email changed; previous and new addresses omitted");
        return UserMapper.toDTO(saved);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequestDTO request) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must differ from the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
        auditService.record(username, "PASSWORD_CHANGED", "user:" + user.getUserId(), "password changed; credential values omitted");
    }

    private AuthResponseDTO buildAuthResponse(UserModel user) {

        String token = jwtService.generateToken(user.getUsername(), user.getTokenVersion());
        com.sims.backend.enums.Role effectiveRole = user.getRole() == com.sims.backend.enums.Role.SUPER_ADMIN
                ? com.sims.backend.enums.Role.ADMIN
                : user.getRole();

        return AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(effectiveRole)
                .enabled(user.getEnabled())
                .build();
    }
}
