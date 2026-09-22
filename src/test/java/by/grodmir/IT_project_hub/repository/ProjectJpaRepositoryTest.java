package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProjectJpaRepositoryTest {
    private final ProjectJpaRepository projectJpaRepository;

    private ProjectJpaEntity validProject() {
        ProjectJpaEntity project = new ProjectJpaEntity();
        project.setName("CRM для банка");
        project.setCustomer("ОАО \"Банк\"");
        project.setStartDate(LocalDate.of(2026, 1, 1));
        return project;
    }

    @Test
    void shouldSaveAndFindProject() {
        ProjectJpaEntity saved = projectJpaRepository.save(validProject());

        assertThat(saved.getId()).isNotNull();
        assertThat(projectJpaRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateProject() {
        ProjectJpaEntity project = projectJpaRepository.save(validProject());
        project.setCustomer("Новый заказчик");
        projectJpaRepository.saveAndFlush(project);

        ProjectJpaEntity updated = projectJpaRepository.findById(project.getId()).orElseThrow();
        assertThat(updated.getCustomer()).isEqualTo("Новый заказчик");
    }

    @Test
    void shouldDeleteProject() {
        ProjectJpaEntity project = projectJpaRepository.save(validProject());

        projectJpaRepository.deleteById(project.getId());

        assertThat(projectJpaRepository.existsById(project.getId())).isFalse();
    }

    @Test
    void shouldAllowNullEndDate() {
        ProjectJpaEntity project = validProject();

        ProjectJpaEntity saved = projectJpaRepository.saveAndFlush(project);

        assertThat(saved.getEndDate()).isNull();
    }

    @Test
    void shouldAllowSettingEndDateLaterThanStartDate() {
        ProjectJpaEntity project = validProject();
        project.setEndDate(LocalDate.of(2026, 12, 31));

        ProjectJpaEntity saved = projectJpaRepository.saveAndFlush(project);

        assertThat(saved.getEndDate()).isEqualTo(LocalDate.of(2026, 12, 31));
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        ProjectJpaEntity project = validProject();
        project.setStartDate(LocalDate.of(2026, 5, 1));
        project.setEndDate(LocalDate.of(2026, 1, 1)); // раньше start_date

        assertThatThrownBy(() -> projectJpaRepository.saveAndFlush(project))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectEndDateEqualToStartDate() {
        ProjectJpaEntity project = validProject();
        LocalDate sameDate = LocalDate.of(2026, 5, 1);
        project.setStartDate(sameDate);
        project.setEndDate(sameDate); // CHECK строго "<", равенство тоже нарушение

        assertThatThrownBy(() -> projectJpaRepository.saveAndFlush(project))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectNullName() {
        ProjectJpaEntity project = validProject();
        project.setName(null);

        assertThatThrownBy(() -> projectJpaRepository.saveAndFlush(project))
                .isInstanceOf(Exception.class); // упадёт до похода в БД — Hibernate сам проверит nullable=false
    }

    @Test
    void shouldRejectNullStartDate() {
        ProjectJpaEntity project = validProject();
        project.setStartDate(null);

        assertThatThrownBy(() -> projectJpaRepository.saveAndFlush(project))
                .isInstanceOf(Exception.class);
    }
}
