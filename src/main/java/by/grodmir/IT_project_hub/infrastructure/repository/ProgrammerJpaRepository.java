package by.grodmir.IT_project_hub.infrastructure.repository;

import by.grodmir.IT_project_hub.infrastructure.entity.ProgrammerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammerJpaRepository extends JpaRepository<ProgrammerJpaEntity, Long> {
}
