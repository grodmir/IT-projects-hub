package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.exception.UsernameAlreadyTakenException;
import by.grodmir.IT_project_hub.domain.model.Role;
import by.grodmir.IT_project_hub.domain.model.User;
import by.grodmir.IT_project_hub.domain.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository, new FakePasswordEncoder());
    }

    @Test
    void register_hashesPassword_andAssignsDefaultRole() {
        User user = userService.register("Ivan Ivanov", "ivan", "secret");

        assertThat(user.id()).isNotNull();
        assertThat(user.username()).isEqualTo("ivan");
        assertThat(user.password()).isEqualTo("hash:secret");
        assertThat(user.role()).isEqualTo(Role.USER);
    }

    @Test
    void register_normalizesUsername() {
        User user = userService.register("Ivan", "  IVAN  ", "secret");

        assertThat(user.username()).isEqualTo("ivan");
    }

    @Test
    void register_throws_whenUsernameAlreadyTaken() {
        userService.register("First", "ivan", "secret1");

        assertThatThrownBy(() -> userService.register("Second", "IVAN", "secret2"))
                .isInstanceOf(UsernameAlreadyTakenException.class);
    }
}
