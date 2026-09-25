package by.grodmir.IT_project_hub.domain.model;

import by.grodmir.IT_project_hub.domain.exception.InvalidEmploymentPeriodException;
import by.grodmir.IT_project_hub.domain.exception.InvalidHourlyRateException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;

public record Programmer(
        Long id,
        Long projectId,
        String lastName,
        String firstName,
        String middleName,
        String position,
        LocalDate workStartDate,
        LocalDate workEndDate,
        BigDecimal hourlyRate,
        boolean fullTime
) {
    private static final BigDecimal HOURS_FULL_TIME = BigDecimal.valueOf(8);
    private static final BigDecimal HOURS_PART_TIME = BigDecimal.valueOf(4);
    private static final BigDecimal TAX_MULTIPLIER  = BigDecimal.valueOf(1.77);

    public Programmer {
        if (workStartDate == null) {
            throw new IllegalStateException("Start date cannot be null");
        }
        if (workEndDate != null && workEndDate.isBefore(workStartDate)) {
            throw new InvalidEmploymentPeriodException(workEndDate, workStartDate);
        }
        if (hourlyRate == null || hourlyRate.signum() < 0) {
            throw new InvalidHourlyRateException(hourlyRate);
        }
        hourlyRate = hourlyRate.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateSalary() {
        LocalDate end = workEndDate == null ? LocalDate.now() : workEndDate;
        long workingDays = countWorkingDays(workStartDate, end);

        BigDecimal hoursPerDay = fullTime ? HOURS_FULL_TIME : HOURS_PART_TIME;

        return hourlyRate
                .multiply(hoursPerDay)
                .multiply(BigDecimal.valueOf(workingDays))
                .multiply(TAX_MULTIPLIER)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Programmer withWorkEndDate(LocalDate workEndDate) {
        return new Programmer(
                id,
                projectId,
                lastName,
                firstName,
                middleName,
                position,
                workStartDate,
                workEndDate,
                hourlyRate,
                fullTime
        );
    }

    public Programmer withProjectId(Long projectId) {
        return new Programmer(
                id,
                projectId,
                lastName,
                firstName,
                middleName,
                position,
                workStartDate,
                workEndDate,
                hourlyRate,
                fullTime
        );
    }

    private static long countWorkingDays(LocalDate from, LocalDate to) {
        return from.datesUntil(to.plusDays(1))
                .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY
                && d.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();
    }
}
