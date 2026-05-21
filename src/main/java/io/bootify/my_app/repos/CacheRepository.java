package io.bootify.my_app.repos;

import io.bootify.my_app.domain.CacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CacheRepository extends JpaRepository<CacheEntity, String> {
}
