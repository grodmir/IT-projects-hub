package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.entity.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammerRepository extends JpaRepository<Programmer, Long> {

}
