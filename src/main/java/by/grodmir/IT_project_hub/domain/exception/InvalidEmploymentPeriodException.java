package by.grodmir.IT_project_hub.domain.exception;

import java.time.LocalDate;

public class InvalidEmploymentPeriodException extends RuntimeException {
    public InvalidEmploymentPeriodException(LocalDate startDate, LocalDate endDate) {
        super("Work end date (" + endDate + ") must be after work start date (" + startDate + ").");
    }
}
