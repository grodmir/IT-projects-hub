package by.grodmir.IT_project_hub.infrastructure.repository;

import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, Long> {
}
