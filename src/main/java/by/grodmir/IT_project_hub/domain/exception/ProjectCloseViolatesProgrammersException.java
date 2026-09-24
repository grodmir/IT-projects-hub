package by.grodmir.IT_project_hub.domain.exception;

import by.grodmir.IT_project_hub.domain.model.Programmer;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectCloseViolatesProgrammersException extends RuntimeException {
    private final Long projectId;
    private final List<Long> programmerIds;

    public ProjectCloseViolatesProgrammersException(Long projectId, List<Programmer> programmers) {
        super("Cannot close project %d: %d programmer(s) start after the new end date"
                .formatted(projectId, programmers.size()));
        this.projectId = projectId;
        this.programmerIds = programmers.stream().map(Programmer::id).toList();
    }
}
