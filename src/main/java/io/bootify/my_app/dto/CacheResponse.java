package io.bootify.my_app.dto;

import java.time.OffsetDateTime;

public record CacheResponse<T>(

        boolean success,
        int status,
        String message,
        T value,
        OffsetDateTime timestamp
) {
}
