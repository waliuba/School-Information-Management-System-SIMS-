package com.sims.backend.service;

import com.sims.backend.dto.request.UserRequestDTO;
import com.sims.backend.dto.response.UserResponseDTO;
import com.sims.backend.mapper.UserMapper;
import com.sims.backend.model.UserModel;
import com.sims.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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