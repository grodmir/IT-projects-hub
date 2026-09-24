package by.grodmir.IT_project_hub.domain.exception;

public class ProgrammerNotFoundException extends RuntimeException {
    public ProgrammerNotFoundException(Long programmerId) {
        super("Programmer not found with id: " + programmerId);
    }
}
