package by.grodmir.IT_project_hub.domain.exception;

import java.time.LocalDate;

public class WorkEndAfterProjectException extends RuntimeException {
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
    }
}
