package by.grodmir.IT_project_hub.domain.model;

public record AuthTokens(String accessToken, String refreshToken) {
    public AuthTokens {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be null or empty");
        }
        else if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }
    }
}
