package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.infrastructure.entity.RoleEntity;
import by.grodmir.IT_project_hub.infrastructure.entity.UserJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.repository.UserJpaRepository;
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
public class UserJpaRepositoryTest {
    private final UserJpaRepository userJpaRepository;
    private final TestEntityManager entityManager;

    private UserJpaEntity validUser(String username) {
        UserJpaEntity user = new UserJpaEntity();
        user.setFullName("Иванов Иван Иванович");
        user.setUsername(username);
        user.setPassword("hashed-password-stub");
        user.setRole(RoleEntity.USER);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    @Test
    void shouldSaveAndFindUser() {
        UserJpaEntity saved = userJpaRepository.save(validUser("ivanov"));

        assertThat(saved.getId()).isNotNull();
        assertThat(userJpaRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateUser() {
        UserJpaEntity saved = userJpaRepository.saveAndFlush(validUser("petrov"));

        saved.setFullName("Петров Пётр Петрович");
        userJpaRepository.saveAndFlush(saved);

        UserJpaEntity updated = userJpaRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getFullName()).isEqualTo("Петров Пётр Петрович");
    }

    @Test
    void shouldDeleteUser() {
        UserJpaEntity saved = userJpaRepository.saveAndFlush(validUser("sidorov"));

        userJpaRepository.deleteById(saved.getId());

        assertThat(userJpaRepository.existsById(saved.getId())).isFalse();
    }

    // ========== 2. Уникальность username ==========

    @Test
    void shouldRejectDuplicateUsername() {
        userJpaRepository.saveAndFlush(validUser("duplicate"));
        UserJpaEntity second = validUser("duplicate");

        assertThatThrownBy(() -> userJpaRepository.saveAndFlush(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldAcceptAllValidRoles() {
        assertThat(userJpaRepository.saveAndFlush(validUser("user1")).getRole()).isEqualTo(RoleEntity.USER);
        assertThat(userJpaRepository.saveAndFlush(validUser("admin1")).getRole())
                .isNotNull();

        UserJpaEntity admin = validUser("admin2");
        admin.setRole(RoleEntity.ADMIN);
        assertThat(userJpaRepository.saveAndFlush(admin).getId()).isNotNull();

        UserJpaEntity manager = validUser("manager1");
        manager.setRole(RoleEntity.MANAGER);
        assertThat(userJpaRepository.saveAndFlush(manager).getId()).isNotNull();
    }

    @Test
    void shouldRejectNullUsername() {
        UserJpaEntity user = validUser(null);

        assertThatThrownBy(() -> userJpaRepository.saveAndFlush(user))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldRejectNullPassword() {
        UserJpaEntity user = validUser("nopassword");
        user.setPassword(null);

        assertThatThrownBy(() -> userJpaRepository.saveAndFlush(user))
                .isInstanceOf(Exception.class);
    }


    @Test
    void shouldAutoPopulateCreatedAtOnPersist() {
        UserJpaEntity user = validUser("autotime");
        user.setCreatedAt(null);

        UserJpaEntity saved = userJpaRepository.saveAndFlush(user);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
    }

    @Test
    void shouldIgnoreManuallySetCreatedAt() {
        UserJpaEntity user = validUser("ignoredtime");
        LocalDateTime fakeDate = LocalDateTime.of(2020, 1, 1, 0, 0);
        user.setCreatedAt(fakeDate);

        UserJpaEntity saved = userJpaRepository.saveAndFlush(user);

        assertThat(saved.getCreatedAt()).isNotEqualTo(fakeDate);
        assertThat(saved.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
    }

    @Test
    void shouldNotChangeCreatedAtOnUpdate() {
        UserJpaEntity saved = userJpaRepository.saveAndFlush(validUser("stabletime"));
        LocalDateTime originalCreatedAt = saved.getCreatedAt();

        saved.setFullName("Изменённое имя");
        saved.setCreatedAt(LocalDateTime.of(1999, 1, 1, 0, 0));
        userJpaRepository.saveAndFlush(saved);

        entityManager.clear();

        UserJpaEntity reloaded = userJpaRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getCreatedAt()).isEqualTo(originalCreatedAt);
    }
}
