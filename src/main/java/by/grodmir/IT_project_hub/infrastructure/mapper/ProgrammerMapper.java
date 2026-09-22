package by.grodmir.IT_project_hub.infrastructure.mapper;

import by.grodmir.IT_project_hub.domain.model.Programmer;
import by.grodmir.IT_project_hub.infrastructure.entity.ProgrammerJpaEntity;
import by.grodmir.IT_project_hub.infrastructure.entity.ProjectJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProgrammerMapper {

    public Programmer toModel(ProgrammerJpaEntity entity) {
        return new Programmer(
                entity.getId(),
                entity.getProject() != null ? entity.getProject().getId() : null,
                entity.getLastName(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getPosition(),
                entity.getWorkStartDate(),
                entity.getWorkEndDate(),
                entity.getHourlyRate(),
                entity.isFullTime()
        );
    }

    public ProgrammerJpaEntity toEntity(Programmer model, ProjectJpaEntity project) {
        ProgrammerJpaEntity jpaEntity = new ProgrammerJpaEntity();
        jpaEntity.setId(model.id());
        jpaEntity.setProject(project);
        jpaEntity.setLastName(model.lastName());
        jpaEntity.setFirstName(model.firstName());
        jpaEntity.setMiddleName(model.middleName());
        jpaEntity.setPosition(model.position());
        jpaEntity.setWorkStartDate(model.workStartDate());
        jpaEntity.setWorkEndDate(model.workEndDate());
        jpaEntity.setHourlyRate(model.hourlyRate());
        jpaEntity.setFullTime(model.fullTime());
        return jpaEntity;
    }
}
