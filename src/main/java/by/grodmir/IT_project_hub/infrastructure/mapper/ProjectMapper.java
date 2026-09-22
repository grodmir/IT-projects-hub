package by.grodmir.IT_project_hub.infrastructure.mapper;

import by.grodmir.IT_project_hub.domain.model.Project;
import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectJpaEntity toEntity(Project model) {
        ProjectJpaEntity entity = new ProjectJpaEntity();
        entity.setId(model.id());
        entity.setName(model.name());
        entity.setCustomer(model.customer());
        entity.setStartDate(model.startDate());
        entity.setEndDate(model.endDate());
        return entity;
    }

    public Project toModel(ProjectJpaEntity entity) {
        return new Project(
                entity.getId(),
                entity.getName(),
                entity.getCustomer(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }
}
