package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

@Getter
public class UsernameAlreadyTakenException extends RuntimeException {
    private final String username;

    public UsernameAlreadyTakenException(String username) {
        super("User with this username (" + username + ") already exists");
        this.username = username;
    }
}
