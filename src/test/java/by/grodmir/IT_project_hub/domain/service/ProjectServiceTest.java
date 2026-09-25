package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.exception.ProjectCloseViolatesProgrammersException;
import by.grodmir.IT_project_hub.domain.exception.ProjectNotFoundException;
import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.domain.model.Project;
import by.grodmir.IT_project_hub.domain.repository.InMemoryProgrammerRepository;
import by.grodmir.IT_project_hub.domain.repository.InMemoryProjectRepository;
import by.grodmir.IT_project_hub.domain.service.result.ProjectCloseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectServiceTest {

    private InMemoryProjectRepository projectRepository;
    private InMemoryProgrammerRepository programmerRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = new InMemoryProjectRepository();
        programmerRepository = new InMemoryProgrammerRepository();
        projectService = new ProjectService(projectRepository, programmerRepository);
    }

    @Test
    void closeOrShorten_shortensOpenEndedProgrammers() {
        Project project = projectRepository.save(project(LocalDate.of(2024, 1, 1), null));
        programmerRepository.save(programmer(project.id(), LocalDate.of(2024, 1, 1), null));

        ProjectCloseResult result = projectService.closeOrShorten(
                project.id(), LocalDate.of(2024, 5, 1));

        assertThat(result.project().endDate()).isEqualTo(LocalDate.of(2024, 5, 1));
        assertThat(result.adjustedProgrammers()).hasSize(1);
        assertThat(result.adjustedProgrammers().getFirst().workEndDate())
                .isEqualTo(LocalDate.of(2024, 5, 1));
    }

    @Test
    void closeOrShorten_shortensProgrammersWithLaterEndDate() {
        Project project = projectRepository.save(project(LocalDate.of(2024, 1, 1), null));
        programmerRepository.save(programmer(project.id(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));

        ProjectCloseResult result = projectService.closeOrShorten(
                project.id(), LocalDate.of(2024, 5, 1));

        assertThat(result.adjustedProgrammers()).hasSize(1);
        assertThat(result.adjustedProgrammers().getFirst().workEndDate())
                .isEqualTo(LocalDate.of(2024, 5, 1));
    }

    @Test
    void closeOrShorten_keepsProgrammersWithinNewEndDate() {
        Project project = projectRepository.save(project(LocalDate.of(2024, 1, 1), null));
        programmerRepository.save(programmer(project.id(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1)));

        ProjectCloseResult result = projectService.closeOrShorten(
                project.id(), LocalDate.of(2024, 5, 1));

        assertThat(result.adjustedProgrammers()).isEmpty();
    }

    @Test
    void closeOrShorten_throws_whenProgrammerStartsAfterNewEndDate() {
        Project project = projectRepository.save(project(LocalDate.of(2024, 1, 1), null));
        programmerRepository.save(programmer(project.id(),
                LocalDate.of(2024, 6, 1), null));

        assertThatThrownBy(() ->
                projectService.closeOrShorten(project.id(), LocalDate.of(2024, 5, 1)))
                .isInstanceOf(ProjectCloseViolatesProgrammersException.class);
    }

    @Test
    void closeOrShorten_doesNothing_whenNewDateIsNotShrinking() {
        Project project = projectRepository.save(
                project(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1)));
        programmerRepository.save(programmer(project.id(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1)));

        ProjectCloseResult result = projectService.closeOrShorten(
                project.id(), LocalDate.of(2024, 12, 1));   // расширение

        assertThat(result.project().endDate()).isEqualTo(LocalDate.of(2024, 6, 1));
        assertThat(result.adjustedProgrammers()).isEmpty();
    }

    @Test
    void closeOrShorten_throws_whenProjectNotFound() {
        assertThatThrownBy(() ->
                projectService.closeOrShorten(999L, LocalDate.of(2024, 5, 1)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void calculateCost_doublesSumOfSalaries() {
        Project project = projectRepository.save(project(LocalDate.of(2024, 1, 1), null));
        // 1 рабочий день (1 янв 2024 — пн), 8ч, 100/ч, ×1.77 = 1416.00
        programmerRepository.save(programmerWithRate(project.id(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1),
                new BigDecimal("100.00")));

        BigDecimal cost = projectService.calculateCost(project.id());

        // 1416.00 × 2 = 2832.00
        assertThat(cost).isEqualByComparingTo("2832.00");
    }

    @Test
    void calculateCost_returnsZero_whenNoProgrammers() {
        Project project = projectRepository.save(project(LocalDate.of(2024, 1, 1), null));

        BigDecimal cost = projectService.calculateCost(project.id());

        assertThat(cost).isEqualByComparingTo("0");
    }

    private Project project(LocalDate start, LocalDate end) {
        return new Project(null, "P", "C", start, end);
    }

    private Programmer programmer(Long projectId, LocalDate start, LocalDate end) {
        return programmerWithRate(projectId, start, end, new BigDecimal("10.00"));
    }

    private Programmer programmerWithRate(Long projectId, LocalDate start,
                                          LocalDate end, BigDecimal rate) {
        return new Programmer(null, projectId, "Ivanov", "Ivan", null,
                "Dev", start, end, rate, true);
    }
}