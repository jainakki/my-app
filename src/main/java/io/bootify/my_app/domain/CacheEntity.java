package io.bootify.my_app.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cache_store")
public class CacheEntity {

    @Id
    private String key;

    private String value;

    private OffsetDateTime lastAccessedAt;

    public CacheEntity() {}

    public CacheEntity(String key, String value, OffsetDateTime lastAccessedAt) {
        this.key = key;
        this.value = value;
        this.lastAccessedAt = lastAccessedAt;
    }

    // getters & setters

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public OffsetDateTime getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(OffsetDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}
