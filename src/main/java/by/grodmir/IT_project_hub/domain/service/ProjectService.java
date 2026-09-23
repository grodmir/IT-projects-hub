package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;


}
