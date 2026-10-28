package com.sims.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sims.backend.dtos.authentication.ChangePasswordRequestDTO;
import com.sims.backend.dtos.authentication.RegisterRequestDTO;
import com.sims.backend.enums.Role;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.UserRepository;
import com.sims.backend.security.JwtService;

class AuthServiceSettingsTest {
    private UserRepository users;
    private PasswordEncoder passwords;
    private AuditService audit;
    private AuthService service;

    @BeforeEach
    void setUp() {
        users = mock(UserRepository.class);
        passwords = mock(PasswordEncoder.class);
        audit = mock(AuditService.class);
        service = new AuthService(users, passwords, mock(JwtService.class), audit);
    }

    @Test
    void publicRegistrationCannotChooseAnAdministrativeRole() {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("new-user").email("new@example.test").password("LongPassword1!").role(Role.ADMIN).build();
        when(passwords.encode(request.getPassword())).thenReturn("encoded");
        when(users.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.register(request);

        verify(users).save(argThat(user -> user.getRole() == Role.STUDENT));
    }

    @Test
    void superAdminIsRecognizedAsAdminForAuthentication() {
        UserRepository repo = mock(UserRepository.class);
        CustomUserDetailsService detailsService = new CustomUserDetailsService(repo);
        UserModel user = new UserModel();
        user.setUsername("superadmin");
        user.setPassword("encoded");
        user.setRole(Role.SUPER_ADMIN);
        user.setEnabled(true);
        when(repo.findByUsername("superadmin")).thenReturn(Optional.of(user));

        UserDetails details = detailsService.loadUserByUsername("superadmin");

        assertTrue(details.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(details.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN")));
    }

    @Test
    void passwordChangeIncrementsTokenVersionAndAuditsWithoutCredentialValues() {
        UserModel user = UserModel.builder().userId(14L).username("admin").email("admin@example.test")
                .password("old-hash").role(Role.ADMIN).build();
        user.setTokenVersion(4);
        when(users.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwords.matches("current", "old-hash")).thenReturn(true);
        when(passwords.matches("nextPassword", "old-hash")).thenReturn(false);
        when(passwords.encode("nextPassword")).thenReturn("new-hash");

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword("current");
        request.setNewPassword("nextPassword");
        service.changePassword("admin", request);

        assertEquals(5, user.getTokenVersion());
        verify(users).save(user);
        verify(audit).record("admin", "PASSWORD_CHANGED", "user:14", "password changed; credential values omitted");
    }
}
