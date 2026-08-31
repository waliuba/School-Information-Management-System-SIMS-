package com.sims.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.models.UserModel;
import com.sims.backend.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserModel>>> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long roleId) {
        List<UserModel> users = userService.getUsers(username, status, roleId);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }

        return ResponseEntity.ok(ApiResponse.of("Users retrieved successfully", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserModel>> getUserById(@PathVariable Long userId) {
        UserModel user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(ApiResponse.of("User retrieved successfully", user));
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserModel>> getUserByEmail(@RequestParam String email) {
        UserModel user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(ApiResponse.of("User retrieved successfully", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserModel>> createUser(@RequestBody UserModel user) {
        UserModel createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("User created successfully", createdUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserModel>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserModel user) {
        UserModel updatedUser = userService.updateUser(userId, user);

        if (updatedUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return ResponseEntity.ok(ApiResponse.of("User updated successfully", updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUserById(userId);

        if (!deleted) {
            throw new ResourceNotFoundException("User not found");
        }

        return ResponseEntity.ok(ApiResponse.of("User deleted successfully", null));
    }
}
