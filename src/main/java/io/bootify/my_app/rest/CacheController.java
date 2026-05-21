package io.bootify.my_app.rest;

import io.bootify.my_app.dto.CacheResponse;
import io.bootify.my_app.dto.CacheRequest;
import io.bootify.my_app.service.LRUCacheService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final LRUCacheService cacheService;

    public CacheController(LRUCacheService cacheService) {
        this.cacheService = cacheService;
    }

    // GET /cache/{key}
    @GetMapping("/{key}")
    public ResponseEntity<CacheResponse<String>> get(@PathVariable String key) {

        String value = cacheService.get(key);

        if (value == null) {
            return ResponseEntity.status(404)
                    .body(new CacheResponse<>(
                            false,
                            404,
                            "Key not found",
                            null,
                            OffsetDateTime.now()
                    ));
        }

        return ResponseEntity.ok(
                new CacheResponse<>(
                        true,
                        200,
                        "Success",
                        value,
                        OffsetDateTime.now()
                )
        );
    }

    @PostMapping
    public ResponseEntity<CacheResponse<String>> put(@Valid @RequestBody CacheRequest request) {

        cacheService.put(request.key(), request.value());

        CacheResponse<String> response = new CacheResponse<>(
                true,
                201,
                "Cache entry created/updated successfully",
                request.key(),
                OffsetDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE /cache/{key}
    @DeleteMapping("/{key}")
    public ResponseEntity<CacheResponse<String>> delete(@PathVariable String key) {

        String removedValue = cacheService.delete(key);

        if (removedValue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CacheResponse<>(
                            false,
                            404,
                            "Key not found",
                            null,
                            OffsetDateTime.now()
                    ));
        }

        CacheResponse<String> response = new CacheResponse<>(
                true,
                200,
                "Cache entry deleted successfully",
                key,
                OffsetDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    // DELETE /cache
    @DeleteMapping
    public ResponseEntity<CacheResponse<Void>> clear() {

        cacheService.clear();

        CacheResponse<Void> response = new CacheResponse<>(
                true,
                200,
                "Cache cleared successfully",
                null,
                OffsetDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<CacheResponse<Map<String, String>>> getAll() {

        Map<String, String> data = cacheService.getAll();

        return ResponseEntity.ok(
                new CacheResponse<>(
                        true,
                        200,
                        "Success",
                        data,
                        OffsetDateTime.now()
                )
        );
    }
}
