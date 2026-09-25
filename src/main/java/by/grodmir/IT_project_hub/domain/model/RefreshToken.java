package by.grodmir.IT_project_hub.domain.model;

import java.time.Instant;

public record RefreshToken(
        Long id,
        String token,
        String username,
        Instant expiresAt,
        boolean revoked
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isActive() {
        return !revoked && !isExpired();
    }
}