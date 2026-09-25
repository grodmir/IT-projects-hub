package by.grodmir.IT_project_hub.domain.repository;

import by.grodmir.IT_project_hub.domain.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> storage = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public User save(User user) {
        Long id = user.id() != null ? user.id() : idSequence.incrementAndGet();
        LocalDateTime createdAt = user.createdAt() != null
                ? user.createdAt()
                : LocalDateTime.now();
        User saved = new User(id, user.fullName(), user.username(),
                user.password(), user.role(), createdAt);
        storage.put(id, saved);
        return saved;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(u -> u.username().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}