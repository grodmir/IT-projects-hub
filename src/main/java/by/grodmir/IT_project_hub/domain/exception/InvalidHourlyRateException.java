package by.grodmir.IT_project_hub.domain.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InvalidHourlyRateException extends RuntimeException {
    private final BigDecimal rate;

    public InvalidHourlyRateException(BigDecimal hourlyRate) {
        super("The hourly rate is invalid (" + hourlyRate + ").");
        this.rate = hourlyRate;
    }
}
