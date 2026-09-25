package by.grodmir.IT_project_hub.domain.model;

import java.time.LocalDateTime;

public record User (
        Long id,
        String fullName,
        String username,
        String password,
        Role role,
        LocalDateTime createdAt
) {
    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
