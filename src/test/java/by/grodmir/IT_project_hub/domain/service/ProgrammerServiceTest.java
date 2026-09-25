package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.exception.ProjectNotFoundException;
import by.grodmir.IT_project_hub.domain.exception.WorkEndAfterProjectException;
import by.grodmir.IT_project_hub.domain.exception.WorkStartsBeforeProjectException;
import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.domain.model.Project;
import by.grodmir.IT_project_hub.domain.repository.InMemoryProgrammerRepository;
import by.grodmir.IT_project_hub.domain.repository.InMemoryProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProgrammerServiceTest {

    private InMemoryProjectRepository projectRepository;
    private InMemoryProgrammerRepository programmerRepository;
    private ProgrammerService programmerService;

    @BeforeEach
    void setUp() {
        projectRepository = new InMemoryProjectRepository();
        programmerRepository = new InMemoryProgrammerRepository();
        programmerService = new ProgrammerService(programmerRepository, projectRepository);
    }

    @Test
    void saveWithValidation_saves_whenDatesAreValid() {
        Project project = projectRepository.save(project(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));

        Programmer saved = programmerService.saveWithValidation(programmer(
                project.id(), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 10, 1)));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.workStartDate()).isEqualTo(LocalDate.of(2024, 2, 1));
        assertThat(saved.workEndDate()).isEqualTo(LocalDate.of(2024, 10, 1));
    }

    @Test
    void saveWithValidation_allowsNullEndDate_whenProjectClosed() {
        Project project = projectRepository.save(project(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));

        Programmer saved = programmerService.saveWithValidation(programmer(
                project.id(), LocalDate.of(2024, 2, 1), null));

        assertThat(saved.workEndDate()).isNull();
    }

    @Test
    void saveWithValidation_allowsAnyEndDate_whenProjectOpenEnded() {
        Project project = projectRepository.save(project(
                LocalDate.of(2024, 1, 1), null));

        Programmer saved = programmerService.saveWithValidation(programmer(
                project.id(), LocalDate.of(2024, 2, 1), LocalDate.of(2030, 1, 1)));

        assertThat(saved.workEndDate()).isEqualTo(LocalDate.of(2030, 1, 1));
    }

    @Test
    void saveWithValidation_allowsEndDateEqualToProjectEnd() {
        Project project = projectRepository.save(project(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));

        Programmer saved = programmerService.saveWithValidation(programmer(
                project.id(), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 12, 31)));

        assertThat(saved.workEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    void saveWithValidation_throws_whenWorkStartsBeforeProject() {
        Project project = projectRepository.save(project(
                LocalDate.of(2024, 2, 1), null));

        assertThatThrownBy(() -> programmerService.saveWithValidation(programmer(
                project.id(), LocalDate.of(2024, 1, 1), null)))
                .isInstanceOf(WorkStartsBeforeProjectException.class);
    }

    @Test
    void saveWithValidation_throws_whenWorkEndsAfterProject() {
        Project project = projectRepository.save(project(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1)));

        assertThatThrownBy(() -> programmerService.saveWithValidation(programmer(
                project.id(), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 12, 1))))
                .isInstanceOf(WorkEndAfterProjectException.class);
    }

    @Test
    void saveWithValidation_throws_whenProjectNotFound() {
        assertThatThrownBy(() -> programmerService.saveWithValidation(programmer(
                999L, LocalDate.of(2024, 1, 1), null)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    private Project project(LocalDate start, LocalDate end) {
        return new Project(null, "P", "C", start, end);
    }

    private Programmer programmer(Long projectId, LocalDate start, LocalDate end) {
        return new Programmer(null, projectId, "Ivanov", "Ivan", null,
                "Dev", start, end, new BigDecimal("10.00"), true);
    }
}