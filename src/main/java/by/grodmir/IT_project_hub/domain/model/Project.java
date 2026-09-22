package by.grodmir.IT_project_hub.domain.model;

import java.time.LocalDate;

public record Project(
        Long id,
        String name,
        String customer,
        LocalDate startDate,
        LocalDate endDate
) {}
