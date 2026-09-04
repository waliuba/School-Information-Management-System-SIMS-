package com.sims.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dto.AuthResponseDTO;
import com.sims.backend.dto.LoginRequestDTO;
import com.sims.backend.dto.RegisterRequestDTO;
import com.sims.backend.dtos.user.UserResponseDTO;
import com.sims.backend.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {

        AuthResponseDTO response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> currentUser(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                authService.getCurrentUser(authentication.getName())
        );
    }
}
