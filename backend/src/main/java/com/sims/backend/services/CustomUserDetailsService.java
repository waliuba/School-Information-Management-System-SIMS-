package com.sims.backend.services;

import com.sims.backend.enums.Role;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserModel user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        List<String> effectiveRoles = new ArrayList<>();
        Role role = user.getRole();
        if (role == Role.SUPER_ADMIN) {
            effectiveRoles.add(Role.ADMIN.name());
            effectiveRoles.add(Role.SUPER_ADMIN.name());
        } else {
            effectiveRoles.add(role.name());
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(effectiveRoles.toArray(String[]::new))
                .disabled(!user.getEnabled())
                .build();
    }
}
