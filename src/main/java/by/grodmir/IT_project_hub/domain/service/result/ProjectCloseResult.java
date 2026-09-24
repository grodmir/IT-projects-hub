package by.grodmir.IT_project_hub.domain.service.result;

import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.domain.model.Project;

import java.util.List;

public record ProjectCloseResult(
        Project project,
        List<Programmer> adjustedProgrammers
) {
    public ProjectCloseResult {
        adjustedProgrammers = List.copyOf(adjustedProgrammers);
    }
}
