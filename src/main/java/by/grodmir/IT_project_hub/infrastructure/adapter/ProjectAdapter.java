package by.grodmir.IT_project_hub.infrastructure.adapter;

import by.grodmir.IT_project_hub.domain.model.Project;
import by.grodmir.IT_project_hub.domain.repository.ProjectRepository;
import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.mapper.ProjectMapper;
import by.grodmir.IT_project_hub.infrastructure.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectAdapter implements ProjectRepository {

    private final ProjectJpaRepository repository;
    private final ProjectMapper mapper;

    @Override
    public Project save(Project project) {
        ProjectJpaEntity entity = mapper.toEntity(project);
        ProjectJpaEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Project> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
