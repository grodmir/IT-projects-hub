package by.grodmir.IT_project_hub.repository;

import by.grodmir.IT_project_hub.entity.Programmer;
import by.grodmir.IT_project_hub.entity.Project;
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
public class ProgrammerRepositoryTest {
    private final ProgrammerRepository programmerRepository;
    private final ProjectRepository projectRepository;

    private Project persistedProject() {
        Project project = new Project();
        project.setName("Тестовый проект");
        project.setCustomer("Тестовый заказчик");
        project.setStartDate(LocalDate.of(2026, 1, 1));
        return projectRepository.saveAndFlush(project);
    }

    private Programmer validProgrammer(Project project) {
        Programmer programmer = new Programmer();
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
        Project project = persistedProject();
        Programmer programmer = validProgrammer(project);

        Programmer saved = programmerRepository.save(programmer);
        assertThat(saved.getId()).isNotNull();
        assertThat(programmerRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateProgrammer() {
        Project project = persistedProject();
        Programmer programmer = programmerRepository.saveAndFlush(validProgrammer(project));

        programmer.setHourlyRate(new BigDecimal("35.00"));
        programmerRepository.saveAndFlush(programmer);

        Programmer updated = programmerRepository.findById(programmer.getId()).orElseThrow();
        assertThat(updated.getHourlyRate()).isEqualByComparingTo("35.00");
    }

    @Test
    void shouldDeleteProgrammer() {
        Project project = persistedProject();
        Programmer programmer = programmerRepository.saveAndFlush(validProgrammer(project));

        programmerRepository.deleteById(programmer.getId());
        assertThat(programmerRepository.existsById(programmer.getId())).isFalse();
    }

    @Test
    void shouldAllowNullWorkEndDate() {
        Project project = persistedProject();
        Programmer programmer = validProgrammer(project);
        programmer.setWorkEndDate(null);

        Programmer saved = programmerRepository.saveAndFlush(programmer);
        assertThat(saved.getWorkEndDate()).isNull();
    }

    @Test
    void shouldRejectNegativeHourlyRate() {
        Project project = persistedProject();
        Programmer programmer = validProgrammer(project);
        programmer.setHourlyRate(new BigDecimal("-35.00"));

        assertThatThrownBy(() -> programmerRepository.saveAndFlush(programmer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectWorkEndDateBeforeWorkStartDate() {
        Project project = persistedProject();
        Programmer programmer = validProgrammer(project);
        programmer.setWorkStartDate(LocalDate.of(2026, 6, 1));
        programmer.setWorkEndDate(LocalDate.of(2026, 1, 1)); // раньше start_date

        assertThatThrownBy(() -> programmerRepository.saveAndFlush(programmer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldRejectProgrammerWithoutProject() {
        Programmer programmer = validProgrammer(null);

        assertThatThrownBy(() -> programmerRepository.saveAndFlush(programmer))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldLoadAssociatedProject() {
        Project project = persistedProject();
        Programmer saved = programmerRepository.saveAndFlush(validProgrammer(project));

        Programmer found = programmerRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getProject().getId()).isEqualTo(project.getId());
    }

    @Test
    void shouldCascadeDeleteWhenProjectDeleted() {
        Project project = persistedProject();
        Programmer saved = programmerRepository.saveAndFlush(validProgrammer(project));

        projectRepository.delete(project);
        projectRepository.flush();

        assertThat(programmerRepository.existsById(saved.getId())).isFalse();
    }
}
