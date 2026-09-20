package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
