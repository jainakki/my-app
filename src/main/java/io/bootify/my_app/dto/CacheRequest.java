package io.bootify.my_app.dto;

import jakarta.validation.constraints.NotBlank;

public record CacheRequest(

        @NotBlank(message = "Key is required")
        String key,

        @NotBlank(message = "Value is required")
        String value
) {
}