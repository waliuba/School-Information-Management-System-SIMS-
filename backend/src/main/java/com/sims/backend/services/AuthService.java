package com.sims.backend.services;

import com.sims.backend.dto.AuthResponseDTO;
import com.sims.backend.dto.LoginRequestDTO;
import com.sims.backend.dto.RegisterRequestDTO;
import com.sims.backend.dtos.user.UserResponseDTO;
import com.sims.backend.mappers.UserMapper;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.UserRepository;
import com.sims.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
                .role(request.getRole())
                .enabled(true)
                .build();

        UserModel savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        UserModel user = userRepository
                .findByUsername(request.getUsername())
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

    private AuthResponseDTO buildAuthResponse(UserModel user) {

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
