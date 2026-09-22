package by.grodmir.IT_project_hub.infrastructure.mapper;

import by.grodmir.IT_project_hub.domain.model.Role;
import by.grodmir.IT_project_hub.domain.model.User;
import by.grodmir.IT_project_hub.infrastructure.entity.RoleEntity;
import by.grodmir.IT_project_hub.infrastructure.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserJpaEntity toEntity(User model) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(model.id());
        entity.setFullName(model.fullName());
        entity.setUsername(model.username());
        entity.setPassword(model.password());
        entity.setRole(toEntityRole(model.role()));
        return entity;
    }

    public User toModel(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getUsername(),
                entity.getPassword(),
                toDomainRole(entity.getRole()),
                entity.getCreatedAt()
        );
    }

    private Role toDomainRole(RoleEntity entity) {
        return switch (entity) {
            case USER -> Role.USER;
            case ADMIN -> Role.ADMIN;
            case MANAGER -> Role.MANAGER;
        };
    }

    private RoleEntity toEntityRole(Role role) {
        return switch (role) {
            case USER -> RoleEntity.USER;
            case ADMIN -> RoleEntity.ADMIN;
            case MANAGER -> RoleEntity.MANAGER;
        };
    }
}
