package by.grodmir.IT_project_hub.domain.model;

import by.grodmir.IT_project_hub.domain.exception.InvalidProjectPeriodException;

import java.time.LocalDate;

public record Project(
        Long id,
        String name,
        String customer,
        LocalDate startDate,
        LocalDate endDate
) {
    public Project {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate != null && !endDate.isAfter(startDate)) {
            throw new InvalidProjectPeriodException(startDate, endDate);
        }
    }

    public Project withEndDate(LocalDate newEndDate) {
        return new Project(id, name, customer, startDate, newEndDate);
    }

    public boolean isActiveOn(LocalDate date) {
        return endDate == null || !endDate.isBefore(date);
    }
}
