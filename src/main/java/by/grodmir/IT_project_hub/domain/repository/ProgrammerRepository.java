package by.grodmir.IT_project_hub.domain.repository;

import by.grodmir.IT_project_hub.domain.model.Programmer;

import java.util.List;
import java.util.Optional;

public interface ProgrammerRepository {
    Programmer save(Programmer programmer);
    Optional<Programmer> findById(Long id);
    List<Programmer> findAll();
    void deleteById(Long id);
    List<Programmer> findByProjectId(Long projectId);
}
