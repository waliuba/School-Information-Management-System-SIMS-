package com.sims.backend.models;

import jakarta.persistence.*;
import lombok.*;
import com.sims.backend.enums.Role;

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_USERS_EMAIL", columnNames = "EMAIL"),
        @UniqueConstraint(name = "UK_USERS_USERNAME", columnNames = "USERNAME")
    }
)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 30)
    private Role role;

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = true;
}

