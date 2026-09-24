package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvalidProjectPeriodException extends RuntimeException {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public InvalidProjectPeriodException(LocalDate startDate, LocalDate endDate) {
        super("Project end date (" + endDate + ") must be after start date (" + startDate + ").");
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
