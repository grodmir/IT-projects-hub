package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
