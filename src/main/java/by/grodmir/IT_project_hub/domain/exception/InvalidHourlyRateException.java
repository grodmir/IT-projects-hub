package by.grodmir.IT_project_hub.domain.exception;

import java.math.BigDecimal;

public class InvalidHourlyRateException extends RuntimeException {
    public InvalidHourlyRateException(BigDecimal hourlyRate) {
        super("The hourly rate is invalid (" + hourlyRate + ").");
    }
}
