package io.bootify.my_app.service;

import io.bootify.my_app.domain.CacheEntity;
import io.bootify.my_app.repos.CacheRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.bootify.my_app.config.CacheProperties;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.time.OffsetDateTime.now;

@Service
public class LRUCacheService {

    private final int capacity;

    private final CacheRepository repository;

    private final LinkedHashMap<String, String> cache;

    public LRUCacheService(CacheRepository repository, CacheProperties properties) {
        this.repository = repository;
        this.capacity = properties.getCapacity();

        cache = new LinkedHashMap<>(capacity, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {

                        boolean remove = size() > capacity;

                        if (remove) {
                            repository.deleteById(eldest.getKey());
                        }

                        return remove;
                    }
                };
        // 🔥 Load DB → cache at startup
        repository.findAll().forEach(e -> cache.put(e.getKey(), e.getValue()));
    }





    // PUT
    public void put(String key, String value) {

        cache.put(key, value);

        // sync DB (upsert)
        repository.save(new CacheEntity(key, value, now()));
    }

    // GET
    public String get(String key) {

        String value = cache.get(key);

        if (value != null) {
            repository.findById(key).ifPresent(entity -> {
                entity.setLastAccessedAt(now());
                repository.save(entity);
            });
        }

        return value;
    }

    // DELETE ONE
    public String delete(String key) {

        String removed = cache.remove(key);

        repository.deleteById(key);

        return removed;
    }

    // CLEAR ALL
    public void clear() {
        cache.clear();
        repository.deleteAll();
    }

    // GET ALL
    public Map<String, String> getAll() {
        return new LinkedHashMap<>(cache);
    }
}
