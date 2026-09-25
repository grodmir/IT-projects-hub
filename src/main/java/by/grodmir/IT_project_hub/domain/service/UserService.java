package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.exception.UsernameAlreadyTakenException;
import by.grodmir.IT_project_hub.domain.model.Role;
import by.grodmir.IT_project_hub.domain.model.User;
import by.grodmir.IT_project_hub.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String fullName, String username, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);

        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new UsernameAlreadyTakenException(normalizedUsername);
        }

        String hashPassword = passwordEncoder.encode(rawPassword);

        User user = new User(
                null,
                fullName,
                normalizedUsername,
                hashPassword,
                Role.USER,
                null
        );

        return userRepository.save(user);
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return username.trim().toLowerCase();
    }
}
