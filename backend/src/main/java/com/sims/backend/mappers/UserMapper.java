package com.sims.backend.mapper;

import com.sims.backend.dto.request.UserRequestDTO;
import com.sims.backend.dto.response.UserResponseDTO;
import com.sims.backend.model.UserModel;

public class UserMapper {

    private UserMapper() {
    }

    public static UserModel toEntity(UserRequestDTO dto) {

        return UserModel.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .enabled(true)
                .build();
    }

    public static UserResponseDTO toDTO(UserModel user) {

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
