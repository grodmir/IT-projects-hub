package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.exception.ProjectCloseViolatesProgrammersException;
import by.grodmir.IT_project_hub.domain.exception.ProjectNotFoundException;
import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.domain.model.Project;
import by.grodmir.IT_project_hub.domain.repository.ProgrammerRepository;
import by.grodmir.IT_project_hub.domain.repository.ProjectRepository;
import by.grodmir.IT_project_hub.domain.service.result.ProjectCloseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final BigDecimal COST_MULTIPLIER = BigDecimal.valueOf(2);

    private final ProjectRepository projectRepository;
    private final ProgrammerRepository programmerRepository;

    /**
     * Закрытие или сужение проекта. Игнорирует расширяющую дату в параметре
     * */
    /* TODO аннотация Transactional пока висит на методе класса, хотя правильнее и логичнее
     *   было бы вынести её в usecase, но поскольку сам слой application пока не реализован,
     *   временно оставлю тут */
    @Transactional
    public ProjectCloseResult closeOrShorten(Long projectId, LocalDate newEndDate) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (project.endDate() != null && !project.endDate().isAfter(newEndDate)) {
            return new ProjectCloseResult(project, List.of());
        }

        List<Programmer> programmers = programmerRepository.findByProjectId(projectId);

        List<Programmer> notStarted = programmers
                .stream()
                .filter(p -> p.workStartDate().isAfter(newEndDate))
                .toList();

        if (!notStarted.isEmpty()) {
            throw new ProjectCloseViolatesProgrammersException(projectId, notStarted);
        }

        List<Programmer> adjusted = new ArrayList<>();
        for (Programmer p : programmers) {
            if (p.workEndDate() == null || p.workEndDate().isAfter(newEndDate)) {
                Programmer shortened = p.withWorkEndDate(newEndDate);
                programmerRepository.save(shortened);
                adjusted.add(shortened);
            }
        }

        Project closed = project.withEndDate(newEndDate);
        Project saved = projectRepository.save(closed);

        return new ProjectCloseResult(saved, adjusted);
    }

    /**
     * Расчёт стоимости проекта
     * */
    @Transactional(readOnly = true)
    public BigDecimal calculateCost(Long projectId) {
        List<Programmer> programmers = programmerRepository.findByProjectId(projectId);
        BigDecimal totalSalaries = programmers.stream()
                .map(Programmer::calculateSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalSalaries.multiply(COST_MULTIPLIER);
    }
}
