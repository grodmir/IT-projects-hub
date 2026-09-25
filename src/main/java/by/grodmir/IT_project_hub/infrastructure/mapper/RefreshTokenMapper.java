package by.grodmir.IT_project_hub.infrastructure.mapper;

import by.grodmir.IT_project_hub.domain.model.RefreshToken;
import by.grodmir.IT_project_hub.infrastructure.entity.RefreshTokenJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenJpaEntity toEntity(RefreshToken model) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(model.id());
        entity.setToken(model.token());
        entity.setUsername(model.username());
        entity.setExpiresAt(model.expiresAt());
        entity.setRevoked(model.revoked());
        return entity;
    }

    public RefreshToken toModel(RefreshTokenJpaEntity entity) {
        return new RefreshToken(
                entity.getId(),
                entity.getToken(),
                entity.getUsername(),
                entity.getExpiresAt(),
                entity.isRevoked()
        );
    }
}
