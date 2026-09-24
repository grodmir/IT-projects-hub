package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WorkStartsBeforeProjectException extends RuntimeException {
    private final Long programmerId;
    private final Long projectId;
    private final LocalDate programmerWorkStartDate;
    private final LocalDate projectStartDate;

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
        this.programmerId = programmerId;
        this.projectId = projectId;
        this.programmerWorkStartDate = programmerWorkStartDate;
        this.projectStartDate = projectStartDate;
    }
}
