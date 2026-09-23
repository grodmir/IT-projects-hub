package by.grodmir.IT_project_hub.infrastructure.repository;

import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.infrastructure.entity.ProgrammerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammerJpaRepository extends JpaRepository<ProgrammerJpaEntity, Long> {
    List<ProgrammerJpaEntity> findByProjectId(Long projectId);
}
