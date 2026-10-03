package com.sims.backend.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.dtos.user.TeacherCreateRequestDTO;
import com.sims.backend.dtos.user.UserRequestDTO;
import com.sims.backend.dtos.user.UserResponseDTO;
import com.sims.backend.enums.Role;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeachersController {
    private final UserService userService;

    @GetMapping public ResponseEntity<ApiResponse<List<UserResponseDTO>>> list() {
        return ResponseEntity.ok(ApiResponse.of("Teachers retrieved successfully", userService.getTeachers()));
    }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<UserResponseDTO>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Teacher retrieved successfully", userService.getTeacherById(id)));
    }
    @PostMapping public ResponseEntity<ApiResponse<UserResponseDTO>> create(@Valid @RequestBody TeacherCreateRequestDTO request) {
        UserRequestDTO user = UserRequestDTO.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.TEACHER)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("Teacher created successfully", userService.createUser(user)));
    }
    @PutMapping("/{id}") public ResponseEntity<ApiResponse<UserResponseDTO>> update(@PathVariable Long id, @RequestBody TeacherUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.of("Teacher updated successfully", userService.updateTeacher(id, request.username(), request.email(), request.enabled())));
    }
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.of("Teacher deleted successfully", null));
    }
    public record TeacherUpdateRequest(String username, String email, Boolean enabled) {}
}
