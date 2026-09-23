package by.grodmir.IT_project_hub.domain.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("Project with id " + projectId + " not found");
    }
}
