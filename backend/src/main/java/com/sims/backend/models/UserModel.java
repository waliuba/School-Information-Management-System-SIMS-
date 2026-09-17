package com.sims.backend.models;

import com.sims.backend.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USERS_EMAIL", columnNames = "EMAIL"),
                @UniqueConstraint(name = "UK_USERS_USERNAME", columnNames = "USERNAME")
        }
)
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
    private boolean enabled = true;

    public UserModel() {
    }

    public UserModel(Long userId, String username, String email, String password, Role role, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public static UserModelBuilder builder() {
        return new UserModelBuilder();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = Boolean.TRUE.equals(enabled);
    }

    public static class UserModelBuilder {

        private Long userId;
        private String username;
        private String email;
        private String password;
        private Role role;
        private boolean enabled = true;

        public UserModelBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserModelBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserModelBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserModelBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserModelBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserModelBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserModelBuilder enabled(Boolean enabled) {
            this.enabled = Boolean.TRUE.equals(enabled);
            return this;
        }

        public UserModel build() {
            return new UserModel(userId, username, email, password, role, enabled);
        }
    }
}

