package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken issue(String username);
    RefreshToken rotate(String oldToken);
    void revoke(String token);
}
