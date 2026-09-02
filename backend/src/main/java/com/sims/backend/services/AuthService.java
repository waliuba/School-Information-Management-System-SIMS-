package com.sims.backend.services;

import com.sims.backend.dtos.LoginRequestDTO;
import com.sims.backend.dtos.LoginResponseDTO;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.UserRepository;
import com.sims.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponseDTO login(
            LoginRequestDTO request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserModel user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        String token =
                jwtService.generateToken(
                        user.getUsername()
                );

        return LoginResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
