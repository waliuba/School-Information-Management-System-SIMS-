package com.sims.backend.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sims.backend.dtos.request.UserRequestDTO;
import com.sims.backend.dtos.response.UserResponseDTO;
import com.sims.backend.mapper.UserMapper;
import com.sims.backend.models.UserModel;
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
}