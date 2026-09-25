package by.grodmir.IT_project_hub.domain.repository;

import by.grodmir.IT_project_hub.domain.model.Programmer;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProgrammerRepository implements ProgrammerRepository {

    private final Map<Long, Programmer> storage = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public Programmer save(Programmer programmer) {
        Long id = programmer.id() != null ? programmer.id() : idSequence.incrementAndGet();
        Programmer saved = new Programmer(id, programmer.projectId(),
                programmer.lastName(), programmer.firstName(), programmer.middleName(),
                programmer.position(), programmer.workStartDate(), programmer.workEndDate(),
                programmer.hourlyRate(), programmer.fullTime());
        storage.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Programmer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Programmer> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public List<Programmer> findByProjectId(Long projectId) {
        return storage.values().stream()
                .filter(p -> Objects.equals(p.projectId(), projectId))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}