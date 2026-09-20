package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.entity.Project;
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
public class ProjectRepositoryTest {
    private final ProjectRepository projectRepository;

    private Project validProject() {
        Project project = new Project();
        project.setName("CRM для банка");
        project.setCustomer("ОАО \"Банк\"");
        project.setStartDate(LocalDate.of(2026, 1, 1));
        return project;
    }

    @Test
    void shouldSaveAndFindProject() {
        Project saved = projectRepository.save(validProject());

        assertThat(saved.getId()).isNotNull();
        assertThat(projectRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateProject() {
        Project project = projectRepository.save(validProject());
        project.setCustomer("Новый заказчик");
        projectRepository.saveAndFlush(project);

        Project updated = projectRepository.findById(project.getId()).orElseThrow();
        assertThat(updated.getCustomer()).isEqualTo("Новый заказчик");
    }

    @Test
    void shouldDeleteProject() {
        Project project = projectRepository.save(validProject());

        projectRepository.deleteById(project.getId());

        assertThat(projectRepository.existsById(project.getId())).isFalse();
    }

    @Test
    void shouldAllowNullEndDate() {
        Project project = validProject();

        Project saved = projectRepository.saveAndFlush(project);

        assertThat(saved.getEndDate()).isNull();
    }

    @Test
    void shouldAllowSettingEndDateLaterThanStartDate() {
        Project project = validProject();
        project.setEndDate(LocalDate.of(2026, 12, 31));

        Project saved = projectRepository.saveAndFlush(project);

        assertThat(saved.getEndDate()).isEqualTo(LocalDate.of(2026, 12, 31));
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        Project project = validProject();
        project.setStartDate(LocalDate.of(2026, 5, 1));
        project.setEndDate(LocalDate.of(2026, 1, 1)); // раньше start_date

        assertThatThrownBy(() -> projectRepository.saveAndFlush(project))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectEndDateEqualToStartDate() {
        Project project = validProject();
        LocalDate sameDate = LocalDate.of(2026, 5, 1);
        project.setStartDate(sameDate);
        project.setEndDate(sameDate); // CHECK строго "<", равенство тоже нарушение

        assertThatThrownBy(() -> projectRepository.saveAndFlush(project))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectNullName() {
        Project project = validProject();
        project.setName(null);

        assertThatThrownBy(() -> projectRepository.saveAndFlush(project))
                .isInstanceOf(Exception.class); // упадёт до похода в БД — Hibernate сам проверит nullable=false
    }

    @Test
    void shouldRejectNullStartDate() {
        Project project = validProject();
        project.setStartDate(null);

        assertThatThrownBy(() -> projectRepository.saveAndFlush(project))
                .isInstanceOf(Exception.class);
    }
}
