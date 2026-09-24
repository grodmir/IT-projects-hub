package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {
    private final Long projectId;

    public ProjectNotFoundException(Long projectId) {
        super("Project with id " + projectId + " not found");
        this.projectId = projectId;
    }
}
