package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

@Getter
public class ProgrammerNotFoundException extends RuntimeException {
    private final Long programmerId;

    public ProgrammerNotFoundException(Long programmerId) {
        super("Programmer not found with id: " + programmerId);
        this.programmerId = programmerId;
    }
}
