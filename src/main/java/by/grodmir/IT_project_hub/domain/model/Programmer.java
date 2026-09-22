package by.grodmir.IT_project_hub.domain.model;

import java.math.BigDecimal;
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
) {}
