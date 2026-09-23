package by.grodmir.IT_project_hub.infrastructure.adapter;

import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.domain.repository.ProgrammerRepository;
import by.grodmir.IT_project_hub.infrastructure.entity.ProgrammerJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.mapper.ProgrammerMapper;
import by.grodmir.IT_project_hub.infrastructure.repository.ProgrammerJpaRepository;
import by.grodmir.IT_project_hub.infrastructure.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProgrammerAdapter implements ProgrammerRepository {
    private final ProgrammerJpaRepository programmerRepository;
    private final ProjectJpaRepository projectRepository;
    private final ProgrammerMapper mapper;

    @Override
    public Programmer save(Programmer programmer) {
        ProjectJpaEntity projectRef = projectRepository.getReferenceById(programmer.projectId());
        ProgrammerJpaEntity entity = mapper.toEntity(programmer, projectRef);
        ProgrammerJpaEntity saved = programmerRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Programmer> findById(Long id) {
        return programmerRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Programmer> findAll() {
        return programmerRepository.findAll().stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        programmerRepository.deleteById(id);
    }

    @Override
    public List<Programmer> findByProjectId(Long projectId) {
        return programmerRepository.findByProjectId(projectId)
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}
