package com.insider.assignment.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class RedisService {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public void setValue(String externalMessageId, String entityType, String value) {
        log.info(
                "Saving value in Redis: externalMessageId={}, entityType={}, value={}",
                externalMessageId,
                entityType,
                value
        );

        try {
            String key = this.createFinalKeyWithMessageId(entityType, externalMessageId);
            reactiveStringRedisTemplate.opsForValue().set(key, value).block();
        } catch (Exception e) {
            log.error("Error saving value in Redis: externalMessageId={}, entityType={}, value={}",
                    externalMessageId,
                    entityType,
                    value,
                    e
            );
        }
    }

    public Mono<String> getValue(String externalMessageId, String entityType) {
        log.info(
                "Getting value from Redis: externalMessageId={}, entityType={}",
                externalMessageId,
                entityType
        );
        try {
            String key = this.createFinalKeyWithMessageId(entityType, externalMessageId);
            return reactiveStringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Error getting value from Redis: externalMessageId={}, entityType={}",
                    externalMessageId,
                    entityType,
                    e
            );
            return Mono.empty();
        }

    }

    private String createFinalKeyWithMessageId(String entityType, String externalMessageId) {
        return String.format("%s:%s", entityType, externalMessageId);
    }
}
