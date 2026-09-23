package by.grodmir.IT_project_hub.domain.exception;

import java.time.LocalDate;

public class InvalidProjectPeriodException extends RuntimeException {
    public InvalidProjectPeriodException(LocalDate startDate, LocalDate endDate) {
        super("Project end date (" + endDate + ") must be after start date (" + startDate + ").");
    }
}
