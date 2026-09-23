package by.grodmir.IT_project_hub.domain.model;

import java.time.LocalDateTime;

public record User (
        Long id,
        String fullName,
        String username,
        String password,
        Role role,
        LocalDateTime createdAt
) {}
