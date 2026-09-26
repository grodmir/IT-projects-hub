package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.exception.ProjectNotFoundException;
import by.grodmir.IT_project_hub.domain.exception.WorkEndAfterProjectException;
import by.grodmir.IT_project_hub.domain.exception.WorkStartsBeforeProjectException;
import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.domain.model.Project;
import by.grodmir.IT_project_hub.domain.repository.ProgrammerRepository;
import by.grodmir.IT_project_hub.domain.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgrammerService {
    private final ProgrammerRepository programmerRepository;
    private final ProjectRepository projectRepository;

    /* TODO аннотация Transactional пока висит на методе класса, хотя правильнее и логичнее
     *   было бы вынести её в usecase, но поскольку сам слой application пока не реализован,
     *   временно оставлю тут */
    @Transactional
    public Programmer saveWithValidation(Programmer programmer) {
        Project project = projectRepository.findById(programmer.projectId()).orElseThrow(
                () -> new ProjectNotFoundException(programmer.projectId())
        );

        validateDatesAgainstProject(programmer, project);

        return programmerRepository.save(programmer);
    }

    private void validateDatesAgainstProject(Programmer programmer, Project project) {
        if (programmer.workStartDate().isBefore(project.startDate())) {
            throw new WorkStartsBeforeProjectException(
                    programmer.id(), project.id(), programmer.workStartDate(), project.startDate()
            );
        }
        if (project.endDate() != null && programmer.workEndDate() != null
                && programmer.workEndDate().isAfter(project.endDate())) {
            throw new WorkEndAfterProjectException(
                    programmer.id(), project.id(), programmer.workEndDate(), project.endDate()
            );
        }
    }
}
