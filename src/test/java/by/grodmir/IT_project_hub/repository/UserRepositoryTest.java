package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.temporal.ChronoUnit;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {
    private final UserRepository userRepository;
    private final TestEntityManager entityManager;

    private User validUser(String username) {
        User user = new User();
        user.setFullName("Иванов Иван Иванович");
        user.setUsername(username);
        user.setPassword("hashed-password-stub");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    @Test
    void shouldSaveAndFindUser() {
        User saved = userRepository.save(validUser("ivanov"));

        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateUser() {
        User saved = userRepository.saveAndFlush(validUser("petrov"));

        saved.setFullName("Петров Пётр Петрович");
        userRepository.saveAndFlush(saved);

        User updated = userRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getFullName()).isEqualTo("Петров Пётр Петрович");
    }

    @Test
    void shouldDeleteUser() {
        User saved = userRepository.saveAndFlush(validUser("sidorov"));

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.existsById(saved.getId())).isFalse();
    }

    // ========== 2. Уникальность username ==========

    @Test
    void shouldRejectDuplicateUsername() {
        userRepository.saveAndFlush(validUser("duplicate"));
        User second = validUser("duplicate");

        assertThatThrownBy(() -> userRepository.saveAndFlush(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectInvalidRole() {
        User user = validUser("wrongrole");
        user.setRole("SUPERADMIN");

        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldAcceptAllValidRoles() {
        assertThat(userRepository.saveAndFlush(validUser("user1")).getRole()).isEqualTo("USER");
        assertThat(userRepository.saveAndFlush(validUser("admin1")).getRole())
                .isNotNull();

        User admin = validUser("admin2");
        admin.setRole("ADMIN");
        assertThat(userRepository.saveAndFlush(admin).getId()).isNotNull();

        User manager = validUser("manager1");
        manager.setRole("MANAGER");
        assertThat(userRepository.saveAndFlush(manager).getId()).isNotNull();
    }

    @Test
    void shouldRejectNullUsername() {
        User user = validUser(null);

        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldRejectNullPassword() {
        User user = validUser("nopassword");
        user.setPassword(null);

        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
                .isInstanceOf(Exception.class);
    }


    @Test
    void shouldAutoPopulateCreatedAtOnPersist() {
        User user = validUser("autotime");
        user.setCreatedAt(null);

        User saved = userRepository.saveAndFlush(user);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
    }

    @Test
    void shouldIgnoreManuallySetCreatedAt() {
        User user = validUser("ignoredtime");
        LocalDateTime fakeDate = LocalDateTime.of(2020, 1, 1, 0, 0);
        user.setCreatedAt(fakeDate);

        User saved = userRepository.saveAndFlush(user);

        assertThat(saved.getCreatedAt()).isNotEqualTo(fakeDate);
        assertThat(saved.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
    }

    @Test
    void shouldNotChangeCreatedAtOnUpdate() {
        User saved = userRepository.saveAndFlush(validUser("stabletime"));
        LocalDateTime originalCreatedAt = saved.getCreatedAt();

        saved.setFullName("Изменённое имя");
        saved.setCreatedAt(LocalDateTime.of(1999, 1, 1, 0, 0));
        userRepository.saveAndFlush(saved);

        entityManager.clear();

        User reloaded = userRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getCreatedAt()).isEqualTo(originalCreatedAt);
    }
}
