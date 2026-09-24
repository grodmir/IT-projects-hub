package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvalidEmploymentPeriodException extends RuntimeException {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public InvalidEmploymentPeriodException(LocalDate startDate, LocalDate endDate) {
        super("Work end date (" + endDate + ") must be after work start date (" + startDate + ").");
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
