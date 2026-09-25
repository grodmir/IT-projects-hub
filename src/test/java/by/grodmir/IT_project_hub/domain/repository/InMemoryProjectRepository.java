package by.grodmir.IT_project_hub.domain.repository;

import by.grodmir.IT_project_hub.domain.model.Project;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProjectRepository implements ProjectRepository {

    private final Map<Long, Project> storage = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public Project save(Project project) {
        Long id = project.id() != null ? project.id() : idSequence.incrementAndGet();
        Project saved = new Project(id, project.name(), project.customer(),
                project.startDate(), project.endDate());
        storage.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Project> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}