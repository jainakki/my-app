package io.bootify.my_app.service;

import io.bootify.my_app.config.CacheProperties;
import io.bootify.my_app.repos.CacheRepository;
import io.bootify.my_app.service.LRUCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LRUCacheServiceTest {

    @Mock
    private CacheRepository repository;

    @Mock
    private CacheProperties properties;

    private LRUCacheService cacheService;

    @BeforeEach
    void setUp() {
        // Arrange capacity config
        when(properties.getCapacity()).thenReturn(2);

        // Simulate DB preloaded data
        when(repository.findAll()).thenReturn(List.of());

        // Create service
        cacheService = new LRUCacheService(repository, properties);
    }


    @Test
    void put_shouldEvictLeastRecentlyUsed_whenCapacityExceeded() {

        // act
        cacheService.put("A", "Apple");
        cacheService.put("B", "Banana");

        // access A so B becomes LRU
        cacheService.get("A");

        // add C → should evict B
        cacheService.put("C", "Cat");

        // assert
        assertNotNull(cacheService.get("A"));
        assertNotNull(cacheService.get("C"));
        assertNull(cacheService.get("B"));

        // verify DB interactions
        verify(repository, times(3)).save(any());
    }


    @Test
    void get_shouldReturnNull_whenKeyDoesNotExist() {

        // act
        String result = cacheService.get("UNKNOWN");

        // assert
        assertNull(result);

        // verify no DB interaction for missing key
        verify(repository, never()).findById(any());
    }

}