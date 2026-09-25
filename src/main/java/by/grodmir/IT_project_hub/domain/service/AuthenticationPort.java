package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.model.AuthTokens;

public interface AuthenticationPort {
    AuthTokens authenticate(String username, String rawPassword);
    AuthTokens refresh(String refreshToken);
    void revoke(String refreshToken);
}
