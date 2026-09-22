package by.grodmir.IT_project_hub.infrastructure.repository;

import by.grodmir.IT_project_hub.infrastructure.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
}
