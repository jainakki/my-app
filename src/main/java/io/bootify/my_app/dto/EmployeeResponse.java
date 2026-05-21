package io.bootify.my_app.dto;

import java.time.OffsetDateTime;

public record EmployeeResponse(

        Long id,
        String name,

        Long departmentId,
        String departmentName,

        OffsetDateTime dateCreated,
        OffsetDateTime lastUpdated

) {}
