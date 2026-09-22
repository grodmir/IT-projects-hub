package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.infrastructure.entity.ProgrammerJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.repository.ProgrammerJpaRepository;
import by.grodmir.IT_project_hub.infrastructure.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProgrammerJpaRepositoryTest {
    private final ProgrammerJpaRepository programmerJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;

    private ProjectJpaEntity persistedProject() {
        ProjectJpaEntity project = new ProjectJpaEntity();
        project.setName("Тестовый проект");
        project.setCustomer("Тестовый заказчик");
        project.setStartDate(LocalDate.of(2026, 1, 1));
        return projectJpaRepository.saveAndFlush(project);
    }

    private ProgrammerJpaEntity validProgrammer(ProjectJpaEntity project) {
        ProgrammerJpaEntity programmer = new ProgrammerJpaEntity();
        programmer.setProject(project);
        programmer.setLastName("Иванов");
        programmer.setFirstName("Иван");
        programmer.setMiddleName("Иванович");
        programmer.setPosition("Разработчик");
        programmer.setWorkStartDate(LocalDate.of(2026, 1, 5));
        programmer.setHourlyRate(new BigDecimal("20.00"));
        programmer.setFullTime(true);
        return programmer;
    }

    @Test
    void shouldSaveAndFindProgrammer() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity programmer = validProgrammer(project);

        ProgrammerJpaEntity saved = programmerJpaRepository.save(programmer);
        assertThat(saved.getId()).isNotNull();
        assertThat(programmerJpaRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateProgrammer() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity programmer = programmerJpaRepository.saveAndFlush(validProgrammer(project));

        programmer.setHourlyRate(new BigDecimal("35.00"));
        programmerJpaRepository.saveAndFlush(programmer);

        ProgrammerJpaEntity updated = programmerJpaRepository.findById(programmer.getId()).orElseThrow();
        assertThat(updated.getHourlyRate()).isEqualByComparingTo("35.00");
    }

    @Test
    void shouldDeleteProgrammer() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity programmer = programmerJpaRepository.saveAndFlush(validProgrammer(project));

        programmerJpaRepository.deleteById(programmer.getId());
        assertThat(programmerJpaRepository.existsById(programmer.getId())).isFalse();
    }

    @Test
    void shouldAllowNullWorkEndDate() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity programmer = validProgrammer(project);
        programmer.setWorkEndDate(null);

        ProgrammerJpaEntity saved = programmerJpaRepository.saveAndFlush(programmer);
        assertThat(saved.getWorkEndDate()).isNull();
    }

    @Test
    void shouldRejectNegativeHourlyRate() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity programmer = validProgrammer(project);
        programmer.setHourlyRate(new BigDecimal("-35.00"));

        assertThatThrownBy(() -> programmerJpaRepository.saveAndFlush(programmer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectWorkEndDateBeforeWorkStartDate() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity programmer = validProgrammer(project);
        programmer.setWorkStartDate(LocalDate.of(2026, 6, 1));
        programmer.setWorkEndDate(LocalDate.of(2026, 1, 1)); // раньше start_date

        assertThatThrownBy(() -> programmerJpaRepository.saveAndFlush(programmer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectProgrammerWithoutProject() {
        ProgrammerJpaEntity programmer = validProgrammer(null);

        assertThatThrownBy(() -> programmerJpaRepository.saveAndFlush(programmer))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldLoadAssociatedProject() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity saved = programmerJpaRepository.saveAndFlush(validProgrammer(project));

        ProgrammerJpaEntity found = programmerJpaRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getProject().getId()).isEqualTo(project.getId());
    }

    @Test
    void shouldCascadeDeleteWhenProjectDeleted() {
        ProjectJpaEntity project = persistedProject();
        ProgrammerJpaEntity saved = programmerJpaRepository.saveAndFlush(validProgrammer(project));

        projectJpaRepository.delete(project);
        projectJpaRepository.flush();

        assertThat(programmerJpaRepository.existsById(saved.getId())).isFalse();
    }
}
