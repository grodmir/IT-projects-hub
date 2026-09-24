package by.grodmir.IT_project_hub.domain.exception;

import java.time.LocalDate;

public class WorkStartsBeforeProjectException extends RuntimeException {
    public WorkStartsBeforeProjectException(
            Long programmerId,
            Long projectId,
            LocalDate programmerWorkStartDate,
            LocalDate projectStartDate
    ) {
        super(String.format(
                "Programmer with id=%d started working on %s, but project with id=%d starts on %s. " +
                        "Work start date cannot be before project start date.",
                programmerId, programmerWorkStartDate, projectId, projectStartDate
        ));
    }
}
