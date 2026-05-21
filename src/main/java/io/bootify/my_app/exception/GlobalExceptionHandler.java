package io.bootify.my_app.exception;

import io.bootify.my_app.dto.CacheResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CacheResponse<Object>> handleException(Exception ex) {

        CacheResponse<Object> response = new CacheResponse<>(
                false,
                500,
                ex.getMessage(),
                null,
                OffsetDateTime.now()
        );

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CacheResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        CacheResponse<Object> response = new CacheResponse<>(
                false,
                400,
                "Validation failed",
                errors,
                OffsetDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }
}
