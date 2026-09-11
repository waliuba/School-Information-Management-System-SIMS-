package com.sims.backend.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import com.sims.backend.enums.Role;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUser("admin", "admin@sims.com", "Admin@123", Role.ADMIN);
        seedUser("teacher", "teacher@sims.com", "Teacher@123", Role.TEACHER);
        seedUser("student", "student@sims.com", "Student@123", Role.STUDENT);
    }

    private void seedUser(String username, String email, String password, Role role) {
        UserModel user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(email).orElseGet(UserModel::new));

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);

        userRepository.saveAndFlush(user);
    }
}
