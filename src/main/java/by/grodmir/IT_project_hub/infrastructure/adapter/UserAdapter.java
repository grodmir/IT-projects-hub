package by.grodmir.IT_project_hub.infrastructure.adapter;

import by.grodmir.IT_project_hub.domain.model.User;
import by.grodmir.IT_project_hub.domain.repository.UserRepository;
import by.grodmir.IT_project_hub.infrastructure.entity.UserJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.mapper.UserMapper;
import by.grodmir.IT_project_hub.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {
    private final UserJpaRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toModel);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
