package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WorkEndAfterProjectException extends RuntimeException {
    private final Long programmerId;
    private final Long projectId;
    private final LocalDate programmerEndDate;
    private  final LocalDate projectEndDate;


    public WorkEndAfterProjectException(
            Long programmerId,
            Long projectId,
            LocalDate programmerEndDate,
            LocalDate projectEndDate
    ) {
        super(String.format(
                "Programmer with id=%d finishes work on %s, but project with id=%d ends on %s. " +
                        "Work end date cannot be after project end date.",
                programmerId, programmerEndDate, projectId, projectEndDate
        ));
        this.programmerId = programmerId;
        this.projectId = projectId;
        this.programmerEndDate = programmerEndDate;
        this.projectEndDate = projectEndDate;
    }
}
